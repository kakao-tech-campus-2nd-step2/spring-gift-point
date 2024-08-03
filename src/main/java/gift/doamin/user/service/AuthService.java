package gift.doamin.user.service;

import gift.doamin.user.dto.LoginRequest;
import gift.doamin.user.dto.SignUpRequest;
import gift.doamin.user.entity.RefreshToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.exception.InvalidRefreshTokenException;
import gift.doamin.user.exception.InvalidSignUpFormException;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.user.repository.RefreshTokenRepository;
import gift.global.util.JwtDto;
import gift.global.util.JwtProvider;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(JpaUserRepository userRepository, PasswordEncoder passwordEncoder,
        JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public JwtDto signUp(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new InvalidSignUpFormException("중복된 이메일은 사용할 수 없습니다");
        }

        String password = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(email, password, null, UserRole.USER);
        user = userRepository.save(user);

        return genrateToken(user);
    }

    public JwtDto login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty() || !passwordEncoder.matches(password, user.get().getPassword())) {
            throw new UserNotFoundException();
        }

        return genrateToken(user.get());
    }

    public String makeNewAccessToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(InvalidRefreshTokenException::new);

        User user = tokenEntity.getUser();
        return jwtProvider.generateAccessToken(user.getId(), user.getRole());
    }

    private JwtDto genrateToken(User user) {
        JwtDto jwt = jwtProvider.generateToken(user.getId(), user.getRole());

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
            .orElseGet(() -> new RefreshToken(jwt.getRefreshToken(), user));

        refreshToken.setToken(jwt.getRefreshToken());

        refreshTokenRepository.save(refreshToken);
        return jwt;
    }
}
