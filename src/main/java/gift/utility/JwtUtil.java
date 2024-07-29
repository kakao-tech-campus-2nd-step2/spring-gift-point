package gift.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(String email) {
        String compactJwt = Jwts.builder()
                                .setSubject(email)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                .signWith(SECRET_KEY)
                                .compact();
        return Base64.getEncoder().encodeToString(compactJwt.getBytes());
    }

    public static String extractEmail(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(decodedToken).getBody();
        return claims.getSubject();
    }

    public static boolean isValidToken(String token) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(token));
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(decodedToken).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
