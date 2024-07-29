package gift.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ExceptionResponse> handleException(ApplicationException e) {
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(400)
                .body(new ExceptionResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}
