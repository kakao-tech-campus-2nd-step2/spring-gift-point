package gift.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TokenService {

    // JWT 토큰 생성 로직 (간단하게 Base64 인코딩으로 구현)
    public String generateToken(String email, String password) {
        return Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
    }

    // 토큰에서 이메일 추출
    public String extractEmailFromToken(String token) {
        String credentials = new String(Base64.getDecoder().decode(token));
        return credentials.split(":")[0];
    }

    // 토큰 디코딩
    public String[] decodeToken(String token) {
        String credentials = new String(Base64.getDecoder().decode(token));
        return credentials.split(":");
    }

    // JWT 토큰 여부 확인 (간단히 .으로 구분된 3개의 파트로 구성된 경우 JWT로 간주)
    public boolean isJwtToken(String token) {
        return token.split("\\.").length == 3;
    }
}