package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

public class KakaoException {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleEtcExceptions(
            AuthenticationException exception
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("KakaoAuthError", "카카오 로그인에 관한 에러가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }
}
