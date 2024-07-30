package gift.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handlerMethodValidationException(HandlerMethodValidationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Error");

        e.getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            problemDetail.setDetail(errorMessage);
        });
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Error");

        e.getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            problemDetail.setDetail(errorMessage);
        });
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> emptyResultDataAccessException(EntityNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatus());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ProblemDetail> duplicateEmailException(DuplicateDataException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatus());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> authenticationException(AuthenticationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatus());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ProblemDetail> authorizationException(AuthorizationException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatus());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(RedissonLockException.class)
    public ResponseEntity<ProblemDetail> handleRedissonLockException(RedissonLockException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatus());
        problemDetail.setTitle(e.getTitle());
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Illegal Argument");
        problemDetail.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> exception(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail("Unknown error");
        log.error("Internal Server Error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
