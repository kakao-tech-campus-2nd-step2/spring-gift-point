package gift.config;

import gift.constant.Constants;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Component
public class JwtConfig {
    public String generateToken(String email) {
        Claims claims = (Claims) Jwts.claims();
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ONE_DAY_MILLIS))
                .signWith(Keys.hmacShaKeyFor(Constants.SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
