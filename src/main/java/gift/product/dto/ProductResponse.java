package gift.product.dto;

import gift.dto.ApiResponse;
import gift.model.HttpResult;
import org.springframework.http.HttpStatus;

public class ProductResponse extends ApiResponse {

    public ProductResponse(HttpResult result, String message, HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
