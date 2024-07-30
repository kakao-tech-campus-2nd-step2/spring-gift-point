package gift.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException methodArgumentNotValidException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(methodArgumentNotValidException.getMessage());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> handleProductException(ProductException productException) {
        return ResponseEntity.status(productException.getHttpStatus())
            .body(productException.getMessage());
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleMemberException(MemberException memberException) {
        return ResponseEntity.status(memberException.getHttpStatus())
            .body(memberException.getMessage());
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<String> handleCategoryException(CategoryException categoryException) {
        return ResponseEntity.status(categoryException.getHttpStatus())
            .body(categoryException.getMessage());
    }

    @ExceptionHandler(OptionException.class)
    public ResponseEntity<String> handleOptionException(OptionException optionException) {
        return ResponseEntity.status(optionException.getHttpStatus())
            .body(optionException.getMessage());
    }
}
