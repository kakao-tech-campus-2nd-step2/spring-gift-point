package gift.global;

import gift.doamin.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final SecretKey key = SIG.HS256.key().build();

    public String generateToken(Long userId, UserRole role) {

        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("role", role)
            .expiration(expiredDate)
            .signWith(key)
            .compact();
    }

    public String generateRefreshToken() {

        Date expiredDate = Date.from(Instant.now().plus(12, ChronoUnit.HOURS));

        return Jwts.builder()
            .expiration(expiredDate)
            .signWith(key)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

}
