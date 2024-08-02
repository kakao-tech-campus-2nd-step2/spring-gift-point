package gift.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JWTUtil {
    private static SecretKey key;
    private static int expirationMs;

    public static void init(String jwtSecret, int jwtExpirationMs) {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        expirationMs = jwtExpirationMs;
    }

    public static String generateToken(int userId, String kakaoToken) {
        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiryDate = Date.from(now.plus(Duration.ofMillis(expirationMs)).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(Integer.toString(userId))
                .claim("kakaoToken", kakaoToken)
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public static boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public static Integer getUserIdFromToken(String token) {
        return Integer.parseInt(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public static String getKakaoTokenFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return (String) claims.get("kakaoToken");
    }
}
