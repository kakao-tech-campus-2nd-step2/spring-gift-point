package gift.common.utils;

import gift.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    @Value("${secret-key}")
    private String secretKey;

    public String generateToken(Member member) {
        return Jwts.builder()
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .subject(member.getId().toString())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            if (token == null) {
                return false;
            }
            Claims payload = getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token) throws JwtException {
        Jws<Claims> jws = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token);
        return jws.getPayload();
    }
}
