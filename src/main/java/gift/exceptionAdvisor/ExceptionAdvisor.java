package gift.exceptionAdvisor;


import gift.dto.ExceptionResponse;
import gift.exceptionAdvisor.exceptions.GiftException;
import gift.exceptionAdvisor.exceptions.MemberServiceException;
import gift.exceptionAdvisor.exceptions.ProductServiceException;
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

    /*
    ProductService 예외 핸들러
    금지된 문구 사용 etc
     */
    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ExceptionResponse> productServiceException(
        ProductServiceException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
            exception.getStatusCode());
    }

    /*
    MemberService 예외 핸들러
    이메일 중복 등
     */
    @ExceptionHandler(MemberServiceException.class)
    public ResponseEntity<ExceptionResponse> memberServiceException(
        MemberServiceException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
            exception.getStatusCode());
    }

    /*
    처리되지 않은 예외 핸들러
    호출되면 안됨
     */
    @ExceptionHandler(GiftException.class)
    public ResponseEntity<ExceptionResponse> giftException(GiftException exception) {
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()),
            exception.getStatusCode());
    }
}
