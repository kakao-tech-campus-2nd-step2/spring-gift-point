package gift.exception;

import gift.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검사 실패 예외를 처리한다.
     *
     * @param ex MethodArgumentNotValidException
     * @return 유효성 검사 오류 메시지를 포함하는 응답 엔터티
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, List> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        errors.put("message", new ArrayList<>());
        for (FieldError error : fieldErrors) {

            if (!errors.containsKey(error.getField())) {
                errors.put(error.getField(), new ArrayList<>());
            }

            errors.get(error.getField()).add(error.getDefaultMessage());
            errors.get("message").add(error.getDefaultMessage());

        }

        return ResponseEntity.badRequest().body(errors);
    }


    /**
     * ProductNotFoundException 예외를 처리한다.
     *
     * @param ex ProductNotFoundException
     * @return 예외 메시지를 포함하는 응답 엔터티
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleProductNotFoundException(
            ProductNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    /**
     * AuthException 예외를 처리한다.
     *
     * @param ex AuthException
     * @return 예외 메시지를 포함하는 응답 엔터티
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleAuthException(
            AuthException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }
}
