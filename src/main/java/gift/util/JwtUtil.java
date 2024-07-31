package gift.util;

import gift.domain.Member;
import gift.domain.Member.MemberResponse;
import gift.error.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(MemberResponse member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.id());
        claims.put("email", member.email());

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new UnauthorizedException("Invalid token");
    }

    public Long extractMemberId(HttpServletRequest request) {
        Claims claims = extractAllClaims(extractToken(request));
        Number memberId = (Number) claims.get("id");
        return memberId.longValue();
    }

}
