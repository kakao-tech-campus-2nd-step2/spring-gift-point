package gift.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        try {
            return extractClaim(token, claims -> claims.get("email", String.class));
        } catch (Exception e) {
            logger.error("Error extracting email from token", e);
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("Error parsing token", e);
            return null;
        }
    }
}
