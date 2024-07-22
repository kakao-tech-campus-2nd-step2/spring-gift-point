package gift.global.exception;

import gift.global.exception.custrom.DBException;
import gift.global.exception.custrom.LoginException;
import gift.global.exception.custrom.NotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleElementNotFoundException(NotFoundException e) {
        return createErrorResponseEntity(e.getErrorCode(), Map.of("description", e.getDetails()));
    }

    @ExceptionHandler(DBException.class)
    public ResponseEntity<ErrorResponseDto> handleDBException(DBException e) {
        return createErrorResponseEntity(e.getErrorCode(), Map.of("description", e.getDetails()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        return createErrorResponseEntity(ErrorCode.INVALID_REQUEST, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getFieldErrors().stream()
            .collect(
                HashMap::new,
                (map, fieldError) -> map.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
                ),
                HashMap::putAll
            );

        return createErrorResponseEntity(ErrorCode.INVALID_REQUEST, errors);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e) {
        return createErrorResponseEntity(ErrorCode.JWT_UNAUTHORIZED, Map.of("description", e.getMessage()));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponseDto> handleLoginException(LoginException e) {
        return createErrorResponseEntity(e.getErrorCode(), Map.of("description", e.getDetails()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return createErrorResponseEntity(ErrorCode.INVALID_REQUEST, Map.of("description", e.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, Map.of("description", e.getMessage()));
    }


    private ResponseEntity<ErrorResponseDto> createErrorResponseEntity(ErrorCode errorCode,
        Map<String, String> details) {
        return new ResponseEntity<>(new ErrorResponseDto(errorCode.getMessage(), details),
            errorCode.getHttpStatus());
    }

    record ErrorResponseDto(
        String message,
        Map<String, String> details
    ) {}
}
