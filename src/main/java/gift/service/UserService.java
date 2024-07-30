/*
package gift.service;

import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User loadOneUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User loginOrRegisterUser(String email, String token) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(email, UUID.randomUUID().toString());
            newUser.setKakaoAccessToken(token);
            return userRepository.save(newUser);
        });
        user.setKakaoAccessToken(token);
        return userRepository.save(user);
    }
}

 */