package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getAllErrors()
            .getFirst()
            .getDefaultMessage();

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = CustomException.class)
    public ProblemDetail handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ProblemDetail.forStatusAndDetail(errorCode.getStatus(), errorCode.getMessage());
    }

}
