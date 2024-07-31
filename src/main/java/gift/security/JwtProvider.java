package gift.security;

import gift.common.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKeyString) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private static final long EXPIRATION_TIME = (60 * 1000) * 60 * 24;

    public String generateToken(long id, String email, Role role) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("정상적인 요청이 아닙니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("로그인이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 형식입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("로그인 후 이용가능합니다.");
        }
    }

    public Long extractUserId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

}
