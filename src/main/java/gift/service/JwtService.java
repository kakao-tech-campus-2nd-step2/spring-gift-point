package gift.service;

import gift.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Key signingKey;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", member.getId());
        claims.put("email", member.getEmail());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(member.getId().toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000))
            .signWith(signingKey)
            .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
