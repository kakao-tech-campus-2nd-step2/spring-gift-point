package gift.global.auth.jwt;

import gift.model.member.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public static final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    @Value("${jwt.expiration}")
    private long ACCESS_TOKEN_EXPIRE_TIME; // 7일

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createToken(Long memberId, Role role) {
        Date date = new Date();
        // 이 코드를 통해서 jwt 토큰을 생성할 수 있다.
        String token = BEARER_PREFIX + Jwts.builder()
            .setSubject(String.valueOf(memberId))    // 사용자 식별값
            .claim("roles", role)        // 사용자 권한   앞에는 key, 뒤에는 value 권한값
            .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))    // 만료시간
            .setIssuedAt(date)                    // 발급입
            .signWith(key, SignatureAlgorithm.HS256)    // 암호화 알고리즘 -> 암호화 알고리즘과 키를 넣어줌
            .compact();

        return token;
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        String parsedToken = parseToken(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(parsedToken);
            System.out.println("토큰이 유효합니다.");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Claims getClaims(String token) {
        String parsedToken = parseToken(token);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(parsedToken)
            .getBody();
    }

    private String parseToken(String token) {
        return token.replace(BEARER_PREFIX, "");
    }

}