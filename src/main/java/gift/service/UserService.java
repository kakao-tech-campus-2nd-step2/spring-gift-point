package gift.service;

import gift.domain.model.dto.KakaoTokenResponseDto;
import gift.domain.model.dto.KakaoUserInfo;
import gift.domain.model.dto.TokenResponseDto;
import gift.domain.model.entity.User;
import gift.domain.model.dto.UserRequestDto;
import gift.domain.model.dto.UserResponseDto;
import gift.domain.repository.UserRepository;
import gift.exception.BadCredentialsException;
import gift.exception.DuplicateEmailException;
import gift.exception.NoSuchEmailException;
import gift.util.JwtUtil;
import java.time.LocalDateTime;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final KakaoLoginService kakaoLoginService;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,
        KakaoLoginService kakaoLoginService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoLoginService = kakaoLoginService;
    }

    public UserResponseDto joinUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new DuplicateEmailException("이미 가입한 이메일입니다.");
        }

        String hashedPassword = BCrypt.hashpw(userRequestDto.getPassword(), BCrypt.gensalt());

        User user = new User(userRequestDto.getEmail(), hashedPassword);
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(userRequestDto.getEmail());

        return new UserResponseDto(savedUser, token);
    }

    public TokenResponseDto loginUser(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail())
            .orElseThrow(() -> new NoSuchEmailException("사용자를 찾을 수 없습니다."));

        if (user.getAuthProvider() != User.AuthProvider.LOCAL) {
            throw new BadCredentialsException("소셜 로그인 사용자입니다. 해당 로그인 방식을 이용해주세요.");
        }

        if (!BCrypt.checkpw(userRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new TokenResponseDto(jwtUtil.generateToken(user.getEmail()));
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new NoSuchEmailException("사용자를 찾을 수 없습니다."));
    }

    public TokenResponseDto loginOrRegisterKakaoUser(KakaoTokenResponseDto kakaoToken) {
        KakaoUserInfo kakaoUserInfo = kakaoLoginService.getUserInfo(kakaoToken.getAccessToken());
        String email = kakaoUserInfo.getEmail();

        User user = userRepository.findByEmail(email)
            .map(existingUser -> {
                if (existingUser.getAuthProvider() != User.AuthProvider.KAKAO) {
                    throw new RuntimeException("이미 가입된 이메일입니다.");
                }
                updateKakaoTokenInfo(existingUser, kakaoToken);
                return existingUser;
            })
            .orElseGet(() -> registerNewKakaoUser(kakaoUserInfo, kakaoToken));

        String jwtToken = jwtUtil.generateToken(email);
        return new TokenResponseDto(jwtToken);
    }

    private User registerNewKakaoUser(KakaoUserInfo kakaoUserInfo, KakaoTokenResponseDto kakaoToken) {
        User newUser = new User(
            kakaoUserInfo.getEmail(),
            User.AuthProvider.KAKAO,
            kakaoUserInfo.getId().toString(),
            kakaoToken.getAccessToken(),
            LocalDateTime.now().plusSeconds(kakaoToken.getExpiresIn())
        );
        return userRepository.save(newUser);
    }

    private void updateKakaoTokenInfo(User user, KakaoTokenResponseDto kakaoToken) {
        user.updateKakaoToken(kakaoToken.getAccessToken(), LocalDateTime.now().plusSeconds(kakaoToken.getExpiresIn()));
        userRepository.save(user);
    }
}
