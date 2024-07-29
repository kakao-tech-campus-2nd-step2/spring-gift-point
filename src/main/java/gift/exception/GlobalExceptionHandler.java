package gift.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    static final String NOT_FOUND_MESSAGE = "존재하지 않는 리소스에 대한 접근입니다.";
    static final String INVALID_PRODUCT_NAME_WITH_KAKAO_MESSAGE = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.";
    static final String DUPLICATED_EMAIL_MESSAGE = "이미 존재하는 이메일입니다.";
    static final String DUPLICATED_NAME_MESSAGE = "이미 존재하는 이름입니다.";
    static final String INVALID_LOGIN_INFO_MESSAGE = "로그인 정보가 유효하지 않습니다.";
    static final String INVALID_PAGE_REQUEST_MESSAGE = "요청에 담긴 페이지 정보가 유효하지 않습니다.";
    static final String UNAUTHORIZED_ACCESS_MESSAGE = "인가되지 않은 요청입니다.";
    static final String EXPIRED_JWT_MESSAGE = "인증 정보가 만료되었습니다.";

    @ExceptionHandler(value = NotFoundElementException.class)
    public ResponseEntity<String> notFoundElementExceptionHandling() {
        return new ResponseEntity<>(NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidProductNameWithKAKAOException.class)
    public ResponseEntity<String> invalidProductNameWithKAKAOExceptionHandling() {
        return new ResponseEntity<>(INVALID_PRODUCT_NAME_WITH_KAKAO_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicatedEmailException.class)
    public ResponseEntity<String> duplicatedEmailExceptionHandling() {
        return new ResponseEntity<>(DUPLICATED_EMAIL_MESSAGE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DuplicatedNameException.class)
    public ResponseEntity<String> duplicatedNameExceptionHandling() {
        return new ResponseEntity<>(DUPLICATED_NAME_MESSAGE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidLoginInfoException.class)
    public ResponseEntity<String> invalidLoginInfoExceptionHandling() {
        return new ResponseEntity<>(INVALID_LOGIN_INFO_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnauthorizedAccessException.class)
    public ResponseEntity<String> unauthorizedAccessExceptionHandling() {
        return new ResponseEntity<>(UNAUTHORIZED_ACCESS_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtExceptionHandling() {
        return new ResponseEntity<>(EXPIRED_JWT_MESSAGE, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = InvalidKakaoTokenException.class)
    public ResponseEntity<Void> invalidKakaoTokenExceptionHandling() {
        var headers = new HttpHeaders();
        String redirectLocation = "http://localhost:8080/api/kakao/set-token";
        headers.setLocation(URI.create(redirectLocation));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<String> badRequestExceptionHandling(BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandling(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(builder.toString());
    }

    @ExceptionHandler(value = PropertyReferenceException.class)
    public ResponseEntity<String> propertyReferenceExceptionHandling() {
        return new ResponseEntity<>(INVALID_PAGE_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> internalServerExceptionHandling(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
