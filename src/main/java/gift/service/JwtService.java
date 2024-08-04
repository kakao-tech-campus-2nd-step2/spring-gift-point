package gift.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private SecretKey key = Jwts.SIG.HS256.key().build();

    public JwtService() {
    }


    public String createJWT(String id) {
        return Jwts.builder()
                .claim("id", id)
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 10000))
                .compact();
    }

    public String getJWT() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");

        if (token == null) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }

        return token.replace("Bearer ", "");
    }

    public String getMemberId() {
        String accessToken = getJWT();

        if (accessToken.isEmpty()) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }
        Jws<Claims> jws;

        try {
            jws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken);
        } catch (JwtException e) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }

        return jws.getPayload()
                .get("id", String.class);
    }

    public void validateJWT(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Date expiration = jws.getBody().getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("해당 요청에 대한 권한이 없습니다.");
            }
            return;
        } catch (Exception e) {
            throw new JwtException("해당 요청에 대한 권한이 없습니다.");
        }
    }
}
