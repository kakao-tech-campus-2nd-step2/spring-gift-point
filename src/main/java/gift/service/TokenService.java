package gift.service;

import gift.domain.Member;
import gift.domain.TokenAuth;
import gift.exception.UnAuthorizationException;
import gift.repository.token.TokenSpringDataJpaRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import static gift.exception.ErrorCode.UNAUTHORIZED;

@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenSpringDataJpaRepository tokenRepository;

    private final String secretKey;

    private final long accessTokenValidityInMillis = 15 * 60 * 1000; // 15 minutes
    private final long refreshTokenValidityInMillis = 7 * 24 * 60 * 60 * 1000; // 7 days


    public TokenService(TokenSpringDataJpaRepository tokenRepository,
                        @Value("${jwt.secret-key}") String secretKey) {
        this.tokenRepository = tokenRepository;
        this.secretKey = secretKey;
    }

    @Transactional
    public String saveToken(Member member) {
        String accessToken = generateAccessToken(member);
        String refreshToken = generateRefreshToken(member);
        saveToken(member, accessToken, refreshToken);
        return accessToken;
    }

    @Transactional
    public void saveToken(Member member, String accessToken, String refreshToken) {
        TokenAuth tokenAuth = tokenRepository.findByMember(member)
                .orElse(new TokenAuth());

        updateTokenAuth(tokenAuth, member, accessToken, refreshToken);

        tokenRepository.save(tokenAuth);
    }

    @Transactional
    public String generateAccessToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .signWith(getSecretKey())
                .compact();
    }

    @Transactional
    public String generateRefreshToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .signWith(getSecretKey())
                .compact();
    }

    @Transactional
    public String saveToken(Member member, String accessToken) {
        TokenAuth tokenAuth = tokenRepository.findByMember(member)
                .orElse(new TokenAuth());

        tokenAuth.setMember(member);
        tokenAuth.setAccessToken(accessToken);

        tokenRepository.save(tokenAuth);

        return tokenAuth.getAccessToken();
    }

    public TokenAuth findTokenByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new UnAuthorizationException(UNAUTHORIZED));
    }

    public TokenAuth findTokenByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnAuthorizationException(UNAUTHORIZED));
    }

    public boolean isAccessTokenValid(String accessToken) {
        Optional<TokenAuth> tokenAuthOpt = tokenRepository.findByAccessToken(accessToken);
        if (tokenAuthOpt.isEmpty()) {
            return false;
        }

        TokenAuth tokenAuth = tokenAuthOpt.get();
        Date now = new Date();
        return now.before(tokenAuth.getAccessTokenExpiry());
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        Optional<TokenAuth> tokenAuthOpt = tokenRepository.findByRefreshToken(refreshToken);
        if (tokenAuthOpt.isEmpty()) {
            return false;
        }

        TokenAuth tokenAuth = tokenAuthOpt.get();
        Date now = new Date();
        return now.before(tokenAuth.getRefreshTokenExpiry());
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        TokenAuth tokenAuth = findTokenByRefreshToken(refreshToken);
        Member member = tokenAuth.getMember();

        String newAccessToken = generateAccessToken(member);
        String newRefreshToken = generateRefreshToken(member);

        updateTokenAuth(tokenAuth, member, newAccessToken, newRefreshToken);
        tokenRepository.save(tokenAuth);

        return newAccessToken;
    }

    private void updateTokenAuth(TokenAuth tokenAuth, Member member, String accessToken, String refreshToken) {
        tokenAuth.setMember(member);
        tokenAuth.setAccessToken(accessToken);
        tokenAuth.setRefreshToken(refreshToken);
        tokenAuth.setAccessTokenExpiry(new Date(System.currentTimeMillis() + accessTokenValidityInMillis));
        tokenAuth.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + refreshTokenValidityInMillis));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 스케쥴링 실행
    @Transactional
    public void deleteExpiredTokens() {
        Date now = new Date();
        tokenRepository.deleteAllExpiredSince(now);
    }
}

