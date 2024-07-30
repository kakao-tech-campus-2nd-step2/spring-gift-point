package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// Jwt를 생성하고 검증하는데 사용.
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    // Secret Key를 생성하는 메서드. Base64로 인코딩된 키를 디코딩하여 SecretKey 객체를 생성.
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(email) // 주체 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME)) // 만료 시간 설정
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT Parsing, 클레임 추출
    public Claims extractClaims(String token) {
        if (token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());
        }
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateToken(String token, String username) {
        Claims claims = extractClaims(token);
        return username.equals(claims.getSubject()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getEmailFromToken(String token) {
        return extractClaims(token).getSubject();
    }
}
