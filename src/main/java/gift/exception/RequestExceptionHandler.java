package gift.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMsg = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMsg.append(fieldName).append(": ").append(errorMessage).append("; ");
        });
        String error = errorMsg.toString();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(ex.getMessage()).append("; ");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(ex.getMessage()).append("; ");
        return ResponseEntity.badRequest().body(errorMsg.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(ex.getMessage()).append("; ");
        return ResponseEntity.internalServerError().body(errorMsg.toString());
    }
}