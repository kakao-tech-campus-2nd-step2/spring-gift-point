package gift.web.exception;

import gift.web.exception.forbidden.ForbiddenException;
import gift.web.exception.kakaoapi.KakaoApiException;
import gift.web.exception.notfound.NotFoundException;
import gift.web.exception.unauthorized.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(gift.web.exception.notfound.NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(KakaoApiException.class)
    public ResponseEntity<?> handleKakaoApiException(KakaoApiException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
            ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getMessage()));
    }
}
