package gift.service;

import gift.config.KakaoProperties;
import gift.entity.User;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.UserAuthException;
import gift.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final KakaoProperties kakaoProperties;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService,
        KakaoProperties kakaoProperties) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.kakaoProperties = kakaoProperties;
    }

    public User getUserByToken(String token) {
        String email = tokenService.extractEmail(token);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserAuthException("유효하지 않은 토큰입니다."));
    }

    public void registerUser(String email, String password) {
        userRepository.findByEmail(email)
            .ifPresentOrElse(user -> {
                throw new EmailAlreadyExistsException("이미 존재하는 email입니다.");
            }, () -> {
                User newUser = new User(email, password);
                userRepository.save(newUser);
            });
    }

    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserAuthException("잘못된 로그인입니다."));

        if (!user.samePassword(password)) {
            throw new UserAuthException("잘못된 로그인입니다.");
        }
        return tokenService.generateToken(user.getEmail());
    }

    public void kakaoUserRegister(String email, String password) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User newUser = new User(email, password);
            userRepository.save(newUser);
        }
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserAuthException(id + "에 해당하는 유저가 없습니다."));
    }

    @Transactional
    public void setAccessToken(String accessToken, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserAuthException("유저가 가입되지 않았습니다."));
        user.setAccessToken(accessToken);
    }

    @Transactional
    public void updateUserPoint(Long userId, Integer newPoints, String password) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserAuthException("유저를 찾을 수 없습니다."));

        if (!kakaoProperties.getDefaultPassword().equals(password)) {
            throw new UserAuthException("잘못된 비밀번호입니다.");
        }

        user.updatePoint(newPoints);
    }
}
