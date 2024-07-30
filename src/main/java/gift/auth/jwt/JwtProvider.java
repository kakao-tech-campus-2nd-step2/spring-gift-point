package gift.auth.jwt;

import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.exception.InvalidAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private static final long ACCESSTOKEN_EXPIRATION_TIME = 30 * 60 * 1000;

    private final String key;
    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secretkey}") String keyParam) {
        this.key = keyParam;
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public JwtToken generateToken(User user) {

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESSTOKEN_EXPIRATION_TIME);

        return new JwtToken(Jwts.builder()
            .subject(user.getId().toString())
            .claim("name", user.getName())
            .claim("email", user.getEmail())
            .claim("role", user.getRole())
            .expiration(accessTokenExpiresIn)
            .signWith(secretKey)
            .compact());
    }

    public Claims getAuthentication(String token) {
        try {
            Claims claims = Objects.requireNonNull(parseClaims(token)).getPayload();

            if (claims.getSubject() == null) {
                throw new InvalidAuthException("error.invalid.token");
            }

            return claims;
        } catch (JwtException e) {
            throw new InvalidAuthException("error.invalid.token");
        }
    }

    public Role getAuthorization(String token) {
        try {
            Claims claims = getAuthentication(token);

            if (claims.get("role") == null) {
                throw new InvalidAuthException("error.invalid.token");
            }

            return Role.valueOf((String) claims.get("role"));
        } catch (JwtException e) {
            throw new InvalidAuthException("error.invalid.token");
        }
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
        } catch(ExpiredJwtException ex) {
            throw new InvalidAuthException("error.invalid.token.Expired");
        }
    }
}
