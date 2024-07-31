package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        FieldError firstError = (FieldError) ex.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        String errorMessage = "Unknown error";
        if (firstError != null) {
            errorMessage = firstError.getDefaultMessage();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalAccessException(IllegalStateException ex){
        String errorMessage = ex.getMessage();
        HttpStatus status =  HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex){
        String errorMessage = ex.getMessage();
        HttpStatus status =  HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(ValueAlreadyExistsException.class)
    public ResponseEntity<String> handleValueAlreadyExistsException(ValueAlreadyExistsException ex){
        String errorMessage = ex.getMessage();
        HttpStatus status =  HttpStatus.CONFLICT;
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(ValueNotFoundException.class)
    public ResponseEntity<String> handleValueNotFoundException(ValueNotFoundException ex){
        String errorMessage = ex.getMessage();
        HttpStatus status =  HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(errorMessage, status);
    }
}
