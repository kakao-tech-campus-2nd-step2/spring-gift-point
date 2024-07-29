package gift.security;

import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${key}")
    private String secretKey;

    @Value("${token.expire-length}")
    private long validTime;

    /*
     * 토큰 생성 : Claim => userId
     */
    public Token makeToken(UserRequest user) {
        Long nowMillis = System.currentTimeMillis();
        Date now = new Date(System.currentTimeMillis());

        String accessToken = Jwts.builder()
                .claim("userId", user.getUserId())
                .issuedAt(now)
                .expiration(new Date(nowMillis + validTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        return new Token(accessToken);
    }
    /*
     * 토큰에서 클레임 ( userId ) 추출
     */
    public String getClaimsFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("userId", String.class);
        } catch(Exception e){
            return "null";
        }
    }
}
