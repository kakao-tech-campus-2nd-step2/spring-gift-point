package gift.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 예외 메시지를 이용해 ResponseEntity 생성
     * @param errorMessage String
     * @param status HttpSatus
     * @return ResponseEntity(errorMessage, status)
     */
    protected ResponseEntity<String> buildResponseEntity(String errorMessage, HttpStatus status) {
        return new ResponseEntity<>(errorMessage, status);
    }

    /**
     * 일반적 예외 처리
     * @param ex Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return buildResponseEntity("서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * RuntimeException 처리하는 핸들러
     * @param ex RuntimeException
     * @return ResponseEntity
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return buildResponseEntity("실행 중 오류 발생. \n " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Validation 예외 처리하는 핸들러
     * @param ex ConstraintViolationException
     * @return ResponseEntity
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();
        return buildResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Validation 예외 처리하는 핸들러 - service
     * @param ex MethodArgumentNotValidException
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleMethodArgumentValidException(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return buildResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
