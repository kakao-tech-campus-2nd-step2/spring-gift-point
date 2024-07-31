package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.KakaoProfileRequest;
import gift.main.dto.KakaoToken;
import gift.main.dto.UserJoinRequest;
import gift.main.dto.UserLoginRequest;
import gift.main.entity.ApiToken;
import gift.main.entity.User;
import gift.main.repository.ApiTokenRepository;
import gift.main.repository.UserRepository;
import gift.main.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final ApiTokenService apiTokenService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserService(ApiTokenService apiTokenService, JwtUtil jwtUtil, UserRepository userRepository) {
        this.apiTokenService = apiTokenService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public String joinUser(UserJoinRequest userJoinRequest) {
        if (userRepository.existsByEmail(userJoinRequest.email())) {
            throw new CustomException(ErrorCode.ALREADY_EMAIL);
        }
        User userdto = new User(userJoinRequest);
        User user = userRepository.save(userdto);
        return jwtUtil.createToken(user);
    }

    public String loginUser(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmailAndPassword(userLoginRequest.email(), userLoginRequest.password())
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_LOGIN));
        return jwtUtil.createToken(user);
    }

    @Transactional
    public String loginKakaoUser(KakaoProfileRequest kakaoProfileRequest, KakaoToken kakaoToken) {
        UserJoinRequest userJoinRequest = new UserJoinRequest(kakaoProfileRequest);
        User savedUser = userRepository.findByEmail(userJoinRequest.email())
                .orElseGet(() -> userRepository.save(new User(userJoinRequest)));
        String jwtToken = jwtUtil.createToken(savedUser);

        apiTokenService.saveToken(savedUser, kakaoToken);

        return jwtToken;

    }
}
