package gift.doamin.user.service;

import gift.doamin.user.entity.RefreshToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.InvalidRefreshTokenException;
import gift.doamin.user.repository.RefreshTokenRepository;
import gift.global.util.JwtDto;
import gift.global.util.JwtProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthTokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthTokenService(JwtProvider jwtProvider,
        RefreshTokenRepository refreshTokenRepository) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public String makeNewAccessToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(InvalidRefreshTokenException::new);

        User user = tokenEntity.getUser();
        return jwtProvider.generateAccessToken(user.getId(), user.getRole());
    }

    @Transactional
    public JwtDto genrateToken(User user) {
        JwtDto jwt = jwtProvider.generateToken(user.getId(), user.getRole());

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
            .orElseGet(() -> new RefreshToken(jwt.getRefreshToken(), user));

        refreshToken.setToken(jwt.getRefreshToken());

        return jwt;
    }

}
