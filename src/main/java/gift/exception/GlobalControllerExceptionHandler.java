package gift.exception;

import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {


    //@Valid에서 발생한 예외 처리
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>(ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Product 중복 이름값을 추가했을 때 Handling
    @ExceptionHandler(value = DuplicateProductNameException.class)
    public ResponseEntity<String> handleDuplicatedId(DuplicateProductNameException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = DuplicateUserEmailException.class)
    public ResponseEntity<String> handleDuplicatedEmail(DuplicateUserEmailException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFound(DataNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> handleDataIntegritViolation(ConstraintViolationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFound(MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
