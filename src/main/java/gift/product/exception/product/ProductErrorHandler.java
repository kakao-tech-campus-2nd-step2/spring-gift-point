package gift.product.exception.product;

import gift.common.util.ApiResponse;
import gift.product.application.ProductController;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ProductController.class)
public class ProductErrorHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFound(
            ProductNotFoundException exception
    ) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(ApiResponse.error(exception.getHttpStatus(), exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleProductNoConferred(
            MethodArgumentNotValidException exception
    ) {
        List<String> errorMessages = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(ApiResponse.error((HttpStatus) exception.getStatusCode(), errorMessages));
    }
}
