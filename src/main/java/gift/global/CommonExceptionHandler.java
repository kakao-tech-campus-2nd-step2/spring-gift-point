package gift.global;

import gift.global.validate.InvalidAuthRequestException;
import gift.global.validate.NotFoundException;
import gift.global.validate.TimeOutException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice("gift.controller")
public class CommonExceptionHandler {

    private final View error;

    public CommonExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> badRequestExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
        MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        problemDetail.setProperties(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(problemDetail);
    }

    @ExceptionHandler(InvalidAuthRequestException.class)
    public ResponseEntity<ProblemDetail> invalidAuthRequestExceptionHandler(
        InvalidAuthRequestException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<ProblemDetail> timeOutExceptionHandler(TimeOutException e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
            .body(ProblemDetail.forStatusAndDetail(HttpStatus.REQUEST_TIMEOUT, e.getMessage()));
    }
}
