package gift.doamin.user.service;

import gift.doamin.user.dto.LoginRequest;
import gift.doamin.user.dto.SignUpRequest;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.exception.InvalidSignUpFormException;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.UserRepository;
import gift.global.util.JwtDto;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenService = authTokenService;
    }

    @Transactional
    public JwtDto signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new InvalidSignUpFormException("중복된 이메일은 사용할 수 없습니다");
        }

        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(email, password, null, UserRole.USER);
        user = userRepository.save(user);

        return authTokenService.genrateToken(user);
    }

    @Transactional
    public JwtDto login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new UserNotFoundException();
        }

        return authTokenService.genrateToken(user.get());
    }
}
