package gift.service;

import gift.model.user.User;
import gift.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password) {
        User user = new User(name, email, password, "ROLE_USER");
        save(user);
        return user;
    }

    public User findByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User valid(String email, String password) {
        User user = findByEmail(email);
        if (user == null || !user.validatePassword(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "적절하지 않은 이메일이거나 비밀번호입니다.");
        }
        return user;
    }
}