package gift.service.user;

import gift.dto.point.MyPointResponse;
import gift.dto.user.LoginResponse;
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
            throw new UserAlreadyExistException("이미 가입된 회원입니다.");
        });
        User user = userRequest.toEntity();
        userRepository.save(user);
    }

    public String login(UserRequest.Check userRequest) {
        User user = userRepository.findByEmailAndPassword(userRequest.email(), userRequest.password())
                .orElseThrow(() -> new InvalidUserException("아이디 또는 비밀번호가 일치하지 않습니다."));
        user.isDefaultLogin();
        String token = jwtUtil.generateJWT(user);
        return token;
    }

    public LoginResponse.Info getLoginInfo(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("존재하지 않는 사용자입니다."));
        return new LoginResponse.Info(user.getName(),user.getRole());
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