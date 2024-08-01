package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String secretKey = "F5540B3B03CF931BCE1AEC9A3A8B70596628";
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public String generateToken(Long memberId, String name, String role) {
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("name", name)
                .claim("role", role).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 2400000L))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey)key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }

    public boolean isTokenValid(String token, Member member) {
        Claims claims = extractClaims(token);
        return claims.getSubject().equals(member.getId().toString()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

}