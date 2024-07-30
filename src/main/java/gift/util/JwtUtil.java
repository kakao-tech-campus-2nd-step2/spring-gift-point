package gift.util;

import gift.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    private static final long ACCESS_TOKEN_VALIDITY_MINUTES = 60;
    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 30;

    public TokenResponse generateTokenResponse(User user) {
        return new TokenResponse
                .Builder()
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public String generateAccessToken(User user) {
        return generateToken(user, ACCESS_TOKEN_VALIDITY_MINUTES);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_TOKEN_VALIDITY_DAYS * 24 * 60);
    }

    public String generateToken(User user, Long validityMinutes) {
        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiryDate = Date.from(
                now.plusMinutes(validityMinutes).atZone(ZoneId.systemDefault()).toInstant());

        Claims claims = Jwts.claims()
                .subject(user.getId().toString()) // ID를 subject로 설정
                .add("id", user.getId())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .build();

        Key key = generateKey();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(generateKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Long> getUserId(String token) {
        Claims claims = getClaims(token);
        if (claims == null) return Optional.empty();

        try {
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("email", String.class) : null;
    }

    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims != null ? claims.get("role", String.class) : null;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(generateKey()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
