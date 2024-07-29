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


@Component
public class JWTUtil {

    private final SecretKey key;
    private final int expirationMs;

    public JWTUtil(@Value("${jwt.secretKey}") String jwtSecret,
                   @Value("${jwt.expiredMs}") int jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.expirationMs = jwtExpirationMs;
    }

    public String generateToken(User user, String kakaoToken) {
        LocalDateTime now = LocalDateTime.now();
        Instant nowInstant = now.atZone(ZoneId.systemDefault()).toInstant();
        Instant expireInstant = nowInstant.plus(Duration.ofMillis(expirationMs));

        return Jwts.builder()
                .subject(Integer.toString(user.getId()))
                .claim("kakaoToken", kakaoToken)
                .issuedAt(Date.from(nowInstant))
                .expiration(Date.from(expireInstant))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }

    public String getKakaoTokenFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return (String) claims.get("kakaoToken");
    }
}
