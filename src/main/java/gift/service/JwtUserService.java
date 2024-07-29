package gift.service;

import gift.domain.AppUser;
import gift.dto.user.LoginRequest;
import gift.exception.user.ForbiddenException;
import gift.repository.UserRepository;
import gift.util.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtUserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        AppUser appUser = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("AppUser"));
        if (!appUser.isPasswordCorrect(loginRequest.password())) {
            throw new ForbiddenException("로그인 실패: 비밀번호 불일치");
        }
        return jwtService.createToken(appUser.getId());
    }

    @Transactional
    public String loginOauth(String email, String token) {
        return userRepository.findByEmail(email)
                .map(appUser -> {
                    appUser.setAccessToken(token);
                    userRepository.save(appUser);
                    return jwtService.createToken(appUser.getId());
                })
                .orElse(null);
    }
}
