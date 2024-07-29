package gift.global.security;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import javax.crypto.SecretKey;
import java.util.Date;

@ConfigurationProperties("jwt")
public class JwtUtil {

    private final String secretKey;
    private final Long jwtExpirationInMs;

    @ConstructorBinding
    public JwtUtil(String secretKey, Long jwtExpirationInMs) {
        this.secretKey = secretKey;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long memberId) {
        return Jwts.builder()
                .subject(memberId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey())
                .compact();
    }

    public Long extractId(String token) {
        try {
            return Long.valueOf(Jwts
                    .parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (ExpiredJwtException exception) {
            throw new CustomException(ErrorCode.AUTHENTICATION_EXPIRED);
        } catch (JwtException exception) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

    }

}
