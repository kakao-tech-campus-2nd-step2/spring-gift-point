package gift.web.jwt;

import gift.web.dto.MemberDto;
import gift.web.dto.Token;
import gift.web.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    private final long exp_time = 15 * 60 * 1000;

    private final String secretKey;

    public JwtUtils(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createJWT(MemberDto memberDto) {
        System.out.println(secretKey);
        return Jwts.builder()
            .claim("email", memberDto.email())
            .expiration(new Date(System.currentTimeMillis() + exp_time))
            .signWith(getSigningKey())
            .compact();
    }

    public Claims validateAndGetClaims(Token token) {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(
                    token.token()
                )
                .getPayload();
        } catch (Exception e) {
           throw new UnauthorizedException("토큰이 유효하지 않음");
        }
    }
}