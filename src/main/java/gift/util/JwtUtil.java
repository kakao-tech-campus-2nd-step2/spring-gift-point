package gift.util;

import gift.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.http.HttpStatus;
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
            throw new ServiceException("헤더에 토큰이 존재하지 않거나 잘못된 형식입니다.", HttpStatus.UNAUTHORIZED);
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