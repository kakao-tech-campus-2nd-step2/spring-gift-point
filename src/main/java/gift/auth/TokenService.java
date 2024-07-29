package gift.auth;

import gift.exception.type.ForbiddenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    private static final String MEMBER_ID_CLAIM = "memberId";

    private final SecretKey secretKey;
    private final long accessTokenExpirationMillis;

    public TokenService(TokenProperty tokenProperty) {
        this.secretKey = Keys.hmacShaKeyFor(tokenProperty.getSecretKey().getBytes());
        this.accessTokenExpirationMillis = tokenProperty.getAccessTokenExpirationMillis();
    }

    public String createToken(Long memberId) {
        return Jwts.builder()
                .claim(MEMBER_ID_CLAIM, memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long extractMemberId(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(MEMBER_ID_CLAIM, Long.class);
        } catch (Exception e) {
            throw new ForbiddenException("유효하지 않은 토큰입니다.");
        }
    }
}
