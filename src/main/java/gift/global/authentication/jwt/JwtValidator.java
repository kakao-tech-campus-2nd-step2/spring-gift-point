package gift.global.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    private final String secret;

    public JwtValidator(@Value("${spring.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public Long validateAndParseToken(String rawToken, TokenType type) {
        var token = validateForm(rawToken);
        return Long.valueOf(validateTokenType(token, type));
    }

    public SecretKey secretKey() {
        try {
            return Keys.hmacShaKeyFor(
                MessageDigest.getInstance("SHA-256").digest(secret.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new JwtException("암호화 알고리즘이 존재하지 않습니다.");
        }
    }

    private String validateForm(String rawToken) {
        if (rawToken == null || !rawToken.startsWith("Bearer ")) {
            throw new JwtException("JWT 토큰이 없거나 유효하지 않은 형식입니다.");
        }

        var token = rawToken.substring(7);
        if (token.isEmpty()) {
            throw new JwtException("JWT 토큰이 존재하지 않습니다");
        }

        return token;
    }

    private String validateTokenType(String token, TokenType type) {
        Claims claims = extractClaims(token);
        if (!claims.get("type").equals(type.name())) {
            throw new JwtException("올바르지 않은 토큰 타입");
        }
        return claims.getSubject();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException e) {
            throw new JwtException("토큰을 파싱하는데 실패했습니다.");
        }
    }
}
