package gift.token.model;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

// 멘토님의 피드백을 반영하여 token resolver로 token을 통한 추출 로직을 옮기려고 하였으나,
// 비밀 키를 온전히 TokenComponent에서 관리하고 있었기 때문에 책임을 분산하고자 토큰을 사용하는 모든 클래스가 해당 클래스를 상속받도록 하였습니다.
// 싱글턴 유지를 위해 스프링 빈으로 등록
@Component
public class TokenManager {

    protected static final String BEARER = "Bearer ";

    // 보안을 위해 token을 업데이트할 수 있도록 final로 선언하지 않기
    protected static final String SECRET_KEY_STRING = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    protected final Key secretKey;

    // 기본 생성자를 통해 미리 특정 알고리즘으로 인코딩한 키를 생성
    public TokenManager() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }

    // 인증 방식 + 토큰의 문자열에서 토큰만 추출하는 메서드
    protected String getOnlyToken(String token) {
        // 일단 주기 전에 검증
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
        }
        verifyBearer(token);
        return token.substring(BEARER.length());
    }

    protected void verifyBearer(String token) {
        if (!token.startsWith(BEARER)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 인증 방식입니다.");
        }
    }
}
