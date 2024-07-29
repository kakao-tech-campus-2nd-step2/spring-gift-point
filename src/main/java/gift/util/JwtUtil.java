package gift.util;

import gift.domain.member.entity.Member;
import gift.domain.member.exception.MemberAuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Member member) {
        return Jwts.builder()
            .subject(member.getId().toString())
            .claim("email", member.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
            .signWith(secretKey)
            .compact();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException e) {
            throw new MemberAuthorizationException("토큰 Authorization 실패.");
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.get("email", String.class);
        }
        return null;
    }
}
