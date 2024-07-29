package gift.authentication.infrastructure.configuration;

import gift.authentication.service.TokenProvider;
import gift.core.domain.authentication.Token;
import gift.core.domain.authentication.exception.AuthenticationExpiredException;
import gift.core.domain.authentication.exception.AuthenticationFailedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider implements TokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access}")
    private Long accessTokenExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Override
    public Token generateAccessToken(Long userId) {
        String token = Jwts.builder()
                .header().empty().add(buildHeader()).and()
                .claims(buildPayload(userId))
                .expiration(buildExpirationDate(accessTokenExpiration))
                .signWith(secretKey)
                .compact();
        return Token.of(token);
    }

    @Override
    public Long extractUserId(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("userId", Long.class);
        } catch (ExpiredJwtException exception) {
            throw new AuthenticationExpiredException();
        } catch (JwtException exception) {
            throw new AuthenticationFailedException();
        }
    }

    private Map<String, Object> buildHeader() {
        return Map.of(
                "typ", "JWT",
                "alg", "HS256",
                "regDate", System.currentTimeMillis()
        );
    }

    private Map<String, Object> buildPayload(Long userId) {
        return Map.of(
                "userId", userId
        );
    }

    private Date buildExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
