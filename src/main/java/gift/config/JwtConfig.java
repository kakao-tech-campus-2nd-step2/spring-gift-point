package gift.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import gift.constant.Constants;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtConfig {
    public String generateToken(String email) {
        // Create a claims map
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        // Generate the JWT token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ONE_DAY_MILLIS))
                .signWith(Keys.hmacShaKeyFor(Constants.SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
