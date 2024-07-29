package gift.model;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CreateJwtToken {
    @Value("${secret_key}")
    private String secretKey;

    public String createJwt(Long id, String email){
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("email", email);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }
}
