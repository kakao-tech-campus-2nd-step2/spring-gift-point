package gift.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
    private static final SecretKey key = SIG.HS256.key().build();
    private static final int EXPIRE_TIME = 1;

    public static String generateToken(String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + Duration.ofHours(EXPIRE_TIME).toMillis());

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(key)
            .compact();
    }

    public static Claims decodeJwt(String token) {
        return Jwts.parser()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
