package gift.advice;

import gift.core.exception.APIException;
import gift.core.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleAPIException(APIException exception) {
        return errorResponse(exception.getCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return errorResponse(ErrorCode.INVALID_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError error = exception.getBindingResult().getFieldErrors().getFirst();
        return errorResponse(ErrorCode.INVALID_REQUEST, error.getDefaultMessage());
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(message));
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getMessage()));
    }
}
