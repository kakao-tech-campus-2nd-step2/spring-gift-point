package gift.controller.exception;

import gift.dto.common.apiResponse.ApiResponseBody.FailureBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {

    // JWT의 서명을 검증할 때 유효하지 않은 경우 발생. 이는 토큰이 변조되었거나 잘못된 키로 서명되었을 때 일어날 수 있음.
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<FailureBody> handleSignatureException(SignatureException e) {
        return ApiResponseGenerator.fail(HttpStatus.UNAUTHORIZED, "토큰 변조 또는 잘못된 키",
            String.valueOf(HttpStatus.UNAUTHORIZED.value()));
    }

    // JWT가 만료된 경우 발생. 토큰의 유효 기간이 지났을 때 이 예외가 발생하며, 사용자에게 새로운 토큰을 요청하거나 재인증을 유도함.
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<FailureBody> handleSignatureException(ExpiredJwtException e) {
        return ApiResponseGenerator.fail(HttpStatus.UNAUTHORIZED, "토큰 만료",
            String.valueOf(HttpStatus.UNAUTHORIZED.value()));
    }

    // 지원하지 않는 JWT 형식을 받았을 때 발생. 예를 들어, 암호화된 JWT를 지원하지 않는 시스템에서 이를 처리하려 할 때 발생할 수 있음.
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<FailureBody> handleSignatureException(UnsupportedJwtException e) {
        return ApiResponseGenerator.fail(HttpStatus.UNAUTHORIZED, "지원하지 않는 Jwt형식",
            String.valueOf(HttpStatus.UNAUTHORIZED.value()));
    }

    // JWT의 구조가 올바르지 않을 때 발생. 이는 토큰의 형식이 잘못되었거나 필수 부분이 누락되었을 때 일어날 수 있음.
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<FailureBody> UnsupportedJwtException(MalformedJwtException e) {
        return ApiResponseGenerator.fail(HttpStatus.BAD_REQUEST, "Jwt 구조 오류",
            String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

}
