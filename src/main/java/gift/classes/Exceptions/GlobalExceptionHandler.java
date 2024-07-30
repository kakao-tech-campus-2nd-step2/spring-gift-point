package gift.classes.Exceptions;

import gift.classes.RequestState.RequestStateDTO;
import gift.classes.RequestState.RequestStatus;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Option 예외처리
    @ExceptionHandler(OptionException.class)
    public ResponseEntity<String> handleOptionException(OptionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // Wish 예외처리
    @ExceptionHandler(WishException.class)
    public ResponseEntity<String> handleWishException(WishException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthorizationHeaderNotFoundException(AuthException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 제품이 존재하지 않을 때 예외 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 유효성 검증 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 제약 조건 위반 예외 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
        ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 이메일 중복 예외 처리
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<RequestStateDTO> handleEmailAlreadyExistsException(
        EmailAlreadyExistsException e) {
        RequestStateDTO response = new RequestStateDTO(
            RequestStatus.failed,
            e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 기타 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
