package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String userId){
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    public String extractUserId(String token) {
        Jws<Claims> claims = parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build()
            .parseClaimsJws(token);
    }
}
