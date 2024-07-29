package gift.util;

import gift.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public static String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SECRET_KEY)
            .compact();
    }

    public static void verifyToken(String token) throws Exception {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
        } catch (Exception e) {
            throw new TokenException("잘못된 로그인 정보입니다.");
        }
    }

    public static Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}