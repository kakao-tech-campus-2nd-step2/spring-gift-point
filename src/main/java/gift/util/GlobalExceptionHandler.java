package gift.util;

import gift.product.exception.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<CommonResponse<String>> handleProductException(ProductException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getMessage(), ex.getMessage());
        CommonResponse<String> commonResponse = new CommonResponse<>(
            errorResponse.getMessage(),
            ex.getMessage(),
            false
        );
        return ResponseEntity.badRequest().body(commonResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<String>> handleCustomException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getMessage(), ex.getMessage());
        CommonResponse<String> commonResponse = new CommonResponse<>(
            errorResponse.getMessage(),
            ex.getMessage(),
            false
        );
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(commonResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<String>> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CommonResponse<>(errorResponse.getMessage(), ex.getMessage(), false));
    }
}
