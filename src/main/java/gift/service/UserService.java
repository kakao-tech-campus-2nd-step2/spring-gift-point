package gift.service;

import gift.entity.User;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.UserAuthException;
import gift.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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

    public Integer getUserPoint(User user) {
        return user.getPoint();
    }

    @Transactional
    public void setAccessToken(String accessToken, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserAuthException("유저가 가입되지 않았습니다."));
        user.setAccessToken(accessToken);
    }
}
