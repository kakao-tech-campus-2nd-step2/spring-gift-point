package gift.user.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtExceptionHandler {

    private ResponseEntity<String> createJwtErrorResponse(String errorMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate",
                "Bearer realm=\"access\"");
        return new ResponseEntity<>(errorMessage, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return createJwtErrorResponse("토큰이 만료되었습니다.");
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<String> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        return createJwtErrorResponse("지원하지 않는 토큰 형식입니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        return createJwtErrorResponse("토큰의 형식이 올바르지 않습니다.");
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return createJwtErrorResponse("토큰의 서명이 유효하지 않습니다.");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return createJwtErrorResponse("유효하지 않은 토큰입니다.");

    }
}
