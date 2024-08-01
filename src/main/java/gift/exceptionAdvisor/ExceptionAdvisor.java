package gift.exceptionAdvisor;


import gift.exceptionAdvisor.exceptions.GiftException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {

    /*
    ProductController 유효성 검사 실패 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> productValidationException(
        MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ExceptionResponse(
            exception.getBindingResult().getFieldError().getDefaultMessage()),
            exception.getStatusCode());
    }

    @ExceptionHandler(GiftException.class)
    public ResponseEntity<ExceptionResponse> giftException(GiftException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
            exception.getStatusCode());
    }
}
