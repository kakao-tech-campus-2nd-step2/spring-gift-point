package gift.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> invalidParams = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("rejectedValue", String.valueOf(fieldError.getRejectedValue()));
            error.put("reason", fieldError.getDefaultMessage());
            return error;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("type", "http://localhost:8080/api/members/validation-error");
        response.put("title", "유효하지 않은 요청입니다.");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("invalid-params", invalidParams);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateException(DuplicateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("type", "http://localhost:8080/api/members/duplicate");
        response.put("title", "이미 가입된 회원입니다.");
        response.put("status", String.valueOf(HttpStatus.CONFLICT.value()));
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException 처리: ", ex);
        Map<String, String> response = new HashMap<>();
        response.put("type", "http://localhost:8080/api/members/illegal-argument");
        response.put("title", "유효하지 않은 이메일 or 비밀번호입니다.");
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

