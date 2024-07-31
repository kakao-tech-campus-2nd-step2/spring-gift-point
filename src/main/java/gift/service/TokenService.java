package gift.service;

import gift.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final String SECRET_KEY = "s3cr3tK3yF0rJWTt0k3nG3n3r@ti0n12345678";
    private static final String TOKEN_TYPE = "bearer ";
    private static final Long TOKEN_VALIDITY = 3600000L;

    private final Key key;

    public TokenService() {// 256 bits
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getBearerTokenFromHeader(String header) {
        if (!header.toLowerCase().startsWith(TOKEN_TYPE)) {
            throw new RuntimeException("Invalid token type");
        }
        return header.substring(TOKEN_TYPE.length());
    }

    protected String generateToken(Member member) {
        long now = System.currentTimeMillis();
        return Jwts.builder().setSubject(member.getEmail())
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + TOKEN_VALIDITY)) // 1 hour validity
            .signWith(key).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
}
