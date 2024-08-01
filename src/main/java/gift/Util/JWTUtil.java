package gift.Util;

import gift.entity.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTUtil {

    private static SecretKey key;
    private static int expirationMs;

    public JWTUtil(@Value("${jwt.secretKey}") String jwtSecret,
                   @Value("${jwt.expiredMs}") int jwtExpirationMs) {
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
