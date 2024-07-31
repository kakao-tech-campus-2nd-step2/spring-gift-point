package gift.wish.exception;

import gift.common.util.ApiResponse;
import gift.product.exception.product.ProductNotFoundException;
import gift.wish.application.WishController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = WishController.class)
public class WishErrorHandler {
    @ExceptionHandler(WishCanNotModifyException.class)
    public ResponseEntity<ApiResponse<String>> handleWishCanNotModifyException(WishCanNotModifyException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(WishNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleWishNotFoundException(WishNotFoundException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getHttpStatus(), e.getMessage()));
    }
}
