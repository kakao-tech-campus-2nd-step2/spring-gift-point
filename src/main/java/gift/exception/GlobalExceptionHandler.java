package gift.exception;

import gift.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponseDto> handleLoginException(LoginException e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(e.getMessage(), e.getHttpStatus());
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity AuthException(AuthException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity jwtException(TokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity businessException(BusinessException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}