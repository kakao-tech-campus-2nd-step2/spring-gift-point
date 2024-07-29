package gift.main.util;

import gift.main.entity.Role;
import gift.main.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final String BEARER = "Bearer ";
    private final String AUTHORIZATION = "Authorization";

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(Long id, String name, String email, String password, String role) {
        String token = Jwts.builder()
                .claim("id", id)
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발생시간
                .setExpiration(new Date(System.currentTimeMillis() + 2400000L)) // 소멸시간 셋팅
                .signWith(secretKey) // 시그니처~!
                .compact();

        return BEARER + token;
    }

    public String createToken(User user) {
        String token = Jwts.builder()
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())

                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발생시간
                .setExpiration(new Date(System.currentTimeMillis() + 2400000L)) // 소멸시간 셋팅
                .signWith(secretKey) // 시그니처~!
                .compact();

        return BEARER + token;
    }


    public boolean validateToken(String token) {
        return !isExpired(token);

    }


    public Long getId(String token) {
        return Jwts.parser()//파서 생성
                .verifyWith(secretKey)
                .build()//파서 키 설정과 빌드 완료
                .parseSignedClaims(token)//토큰 서명 확인
                .getPayload()
                .get("id", Long.class);
    }

    public String getEmail(String token) {
        return Jwts.parser()//파서 생성
                .verifyWith(secretKey)
                .build()//파서 키 설정과 빌드 완료
                .parseSignedClaims(token)//토큰 서명 확인
                .getPayload()
                .get("email", String.class);
    }


    public String getName(String token) {
        return Jwts.parser()//파서 생성
                .verifyWith(secretKey)
                .build()//파서 키 설정과 빌드 완료
                .parseSignedClaims(token)//토큰 서명 확인
                .getPayload()
                .get("name", String.class);
    }

    public Role getRole(String token) {
        return Role.toRole(Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload().get("role", String.class));
    }

    private Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());
    }

}
