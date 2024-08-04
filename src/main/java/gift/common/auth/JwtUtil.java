package gift.common.auth;

import gift.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "N4cYFXz4pYr/sdf9p0sl/j4L8kdKd1gdPv+0Wn4fZ8M7aOg4xqKr4sljxfwHt4Kp7op4l+Jd3ZV4Rmd1Jd7AfQ==JASDJWQDWEWQEKSA"; // 비밀 키
    private static final long EXPIRATION_TIME = 86400000; // 24시간

    // JWT 토큰을 파싱하여 그 안에 포함된 클레임을 반환
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 토큰에서 이메일 주소 추출
    public String extractEmail(String token) {
        Claims claims = extractClaims(token);
        return claims.get("email", String.class);
    }

    // 헤더값에서 토큰 추출하기 (Bearer 제거)
    public String extractToken(String authorizationHeader) {
        return authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;
    }


    // JWT 생성
    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail()) // 이메일 클레임 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
