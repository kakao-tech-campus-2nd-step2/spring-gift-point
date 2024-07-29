package gift.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final SecretKey key;
    private static final String PREFIX = "Bearer ";

    public JwtProvider(@Value("${key.jwt.secret-key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(MemberTokenDTO memberTokenDTO) {
        return PREFIX + Jwts.builder()
            .claim("email", memberTokenDTO.getEmail())
            .signWith(key)
            .compact();
    }

    public MemberTokenDTO getMemberTokenDTOFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token.replace(PREFIX, ""))
            .getPayload();

        return new MemberTokenDTO(claims.get("email", String.class));
    }
}
