package gift.product.exception.option;

import gift.common.util.ApiResponse;
import gift.product.application.ProductOptionController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ProductOptionController.class)
public class ProductOptionHandler {
    @ExceptionHandler(ProductOptionNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductOptionNotFoundException(
            ProductOptionNotFoundException e
    ) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(ProductOptionDuplicatedException.class)
    public ResponseEntity<ApiResponse<String>> handleProductOptionDuplicatedException(
            ProductOptionDuplicatedException e
    ) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(ProductOptionNotDeletedException.class)
    public ResponseEntity<ApiResponse<String>> handleProductOptionNotDeletedException(
            ProductOptionNotDeletedException e
    ) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }
}
