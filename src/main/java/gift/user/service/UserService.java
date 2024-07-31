package gift.user.service;

import gift.user.domain.User;
import gift.user.domain.dto.LoginRequest;
import gift.user.domain.dto.LoginResponse;
import gift.user.repository.UserRepository;
import gift.utility.JwtUtil;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateUser(Long id, User user) {
        if(userRepository.existsById(id)){
            user.setId(id);
            userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public LoginResponse save(LoginRequest loginRequest){
        Long userId = userRepository.save(new User(loginRequest.email(), loginRequest.password())).getId();
        return new LoginResponse(getJwtTokenByUserId(userId));
    }

    public String getJwtTokenByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("userId " + userId + "가 없습니다."));
        return JwtUtil.generateToken(user.getEmail());
    }
}
