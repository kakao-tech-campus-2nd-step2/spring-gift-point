package gift.global;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleNotValidMethodArgument(
        MethodArgumentNotValidException exception) {
        Map<String, String> errorMessages = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessages.put(field, message);
        });
        return ResponseEntity.badRequest().body(errorMessages);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
        ResponseStatusException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", exception.getStatusCode().value());
        responseBody.put("error", exception.getStatusCode().toString());
        if (exception.getReason() != null) {
            responseBody.put("message", exception.getReason());
        }
        return ResponseEntity.status(exception.getStatusCode())
            .body(responseBody);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
        IllegalArgumentException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElementException(
        NoSuchElementException exception) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
