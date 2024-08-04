package gift.service;

import gift.domain.Member;
import gift.domain.TokenAuth;
import gift.exception.UnAuthorizationException;
import gift.repository.token.TokenSpringDataJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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

        tokenAuth.setMember(member);
        tokenAuth.setAccessToken(accessToken);
        tokenAuth.setRefreshToken(refreshToken);

        tokenRepository.save(tokenAuth);
    }

    @Transactional
    public String generateAccessToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityInMillis))
                .signWith(getSecretKey())
                .compact();
    }

    @Transactional
    public String generateRefreshToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInMillis))
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
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        TokenAuth tokenAuth = findTokenByRefreshToken(refreshToken);
        Member member = tokenAuth.getMember();
        return generateAccessToken(member);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}

