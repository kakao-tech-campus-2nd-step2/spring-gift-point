package gift.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private String secretKey = "secretKey";
    private final long validityInMilliseconds = 3600000; // 1h

    // JWT 토큰 생성
    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // JWT 토큰 유효성 검증(인증)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            // Jwt 파서 객체 초기화 -> 서명 검증위한 비밀키 설정 -> 유효성 검증 + 파서 객체에 각각(header,payload,signature) 담는다
            return true;
        } catch (JwtException e) {
            throw e;
        }
    }

    // JWT 클레임
    public Claims getClaims(String token) {
        // 토큰 검증 -> payload 추출
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    // token의 subject를 꺼낸다 == 이 토큰을 가진 client의 이름을 꺼낸다 >> 이 문자열로 클라이언트 식별한다!!
    public String getMemberEmail(String token) {
        return getClaims(token).getSubject();
    }
}

