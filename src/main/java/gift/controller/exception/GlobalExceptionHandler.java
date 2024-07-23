package gift.controller.exception;

import gift.dto.common.apiResponse.ApiResponseBody.FailureBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailureBody> handleProductNameLengthExceededException(
        MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().getFirst();
        return ApiResponseGenerator.fail(HttpStatus.BAD_REQUEST,
            fieldError.getField() + " : " + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<FailureBody> handleNoSuchElementException(NoSuchElementException e) {
        return ApiResponseGenerator.fail(HttpStatus.NOT_FOUND, e.getMessage(),
            String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<FailureBody> handleIllegalStateException(IllegalStateException e){
        return ApiResponseGenerator.fail(HttpStatus.FORBIDDEN, e.getMessage(),
            String.valueOf(HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureBody> handleIllegalArgumentException(IllegalArgumentException e){
        return ApiResponseGenerator.fail(HttpStatus.BAD_REQUEST, e.getMessage(),
            String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }
}
