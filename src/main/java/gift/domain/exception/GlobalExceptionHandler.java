package gift.domain.exception;

import com.google.common.base.CaseFormat;
import gift.global.apiResponse.ErrorApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 서버에서 발생하는 예외를 종합적으로 처리하는 클래스
 */
@RestControllerAdvice(basePackages = {"gift.domain", "gift.global"})
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        assert error != null;
        return ErrorApiResponse.of(
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getField()) + ": " + error.getDefaultMessage(),
            ErrorCode.FIELD_VALIDATION_FAIL.getErrorIdentifier(),
            ErrorCode.FIELD_VALIDATION_FAIL.getErrorCode(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorApiResponse> handleServerException(ServerException e) {
        return ErrorApiResponse.of(e, e.getErrorCode().getStatus());
    }
}
