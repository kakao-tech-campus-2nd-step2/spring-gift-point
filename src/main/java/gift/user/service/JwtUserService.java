package gift.user.service;

import gift.user.exception.ForbiddenException;
import gift.user.jwt.JwtService;
import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.LoginRequest;
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
}
