package gift.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("유효하지 않은 입력입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "해당 데이터가 없습니다.");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExistException(AlreadyExistException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleInvalidAuthCodeException(AuthenticationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ProblemDetail> handleJwtException(JwtException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(WishInvalidAuthException.class)
    public ResponseEntity<ProblemDetail> handleWishInvalidAuthException(WishInvalidAuthException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ProblemDetail> handleOutOfStockException(OutOfStockException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }
    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<ProblemDetail> handleNotAdminException(NotAdminException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(LackPointException.class)
    public ResponseEntity<ProblemDetail> handleLackPointException(LackPointException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setDetail(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(problemDetail);
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpClientErrorException(HttpClientErrorException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getStatusCode().toString());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpServerErrorException(HttpServerErrorException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getStatusCode().toString());
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
}

