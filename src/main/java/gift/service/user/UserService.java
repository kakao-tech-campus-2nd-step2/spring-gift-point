package gift.service.user;

import gift.dto.user.UserRequest;
import gift.exception.InvalidUserException;
import gift.exception.UserAlreadyExistException;
import gift.model.user.User;
import gift.repository.user.UserRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void register(UserRequest.Create userRequest) {
        userRepository.findByEmail(userRequest.email()).ifPresent(existUser -> {
            throw new UserAlreadyExistException(existUser.getEmail() + "은 이미 존재하는 이메일입니다.");
        });
        User user = userRequest.toEntity();
        userRepository.save(user);
    }

    public String login(UserRequest.Check userRequest) {
        User user = userRepository.findByEmailAndPassword(userRequest.email(), userRequest.password()).orElseThrow(() -> new InvalidUserException("이메일 혹은 패스워드가 유효하지 않습니다."));
        user.isDefaultLogin();
        String token = jwtUtil.generateJWT(user);
        return token;
    }

    public boolean validateToken(String token) {
        boolean isValidToken = jwtUtil.checkValidateToken(token);
        if (isValidToken) {
            return true;
        }
        throw new InvalidUserException("유효하지 않은 사용자입니다.");
    }

    public User getUserByToken(String token) {
        String email = jwtUtil.getUserEmail(token);
        return userRepository.findByEmail(email).orElseThrow(() -> new InvalidUserException("유효하지 않은 사용자입니다."));

    }
}