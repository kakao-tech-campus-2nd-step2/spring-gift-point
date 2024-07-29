package gift.service;

import gift.exception.ErrorCode;
import gift.model.user.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtProvider {

    private final SecretKey key;
    private final int expirationMs;

    public JwtProvider(@Value("${jwt.secretKey}") String jwtSecret,
        @Value("${jwt.expiredMs}") int jwtExpirationMs) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.expirationMs = jwtExpirationMs;
    }

    public String generateToken(UserDTO userDTO) {
        return Jwts.builder()
            .subject(Long.toString(userDTO.getId()))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(key)
            .compact();
    }

    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.TOKEN_EXPIRED.getMessage());
        } catch (JwtException e) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage());
        }
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}