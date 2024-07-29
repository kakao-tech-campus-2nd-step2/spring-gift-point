package gift.util;

import gift.model.user.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String getUserEmail(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String email = claims.getSubject();
        return email;
    }

    public boolean checkValidateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateJWT(User user) {
        long expirationTime = System.currentTimeMillis() + 3600000;
        Date expirationDate = new Date(expirationTime);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return token;
    }
}
