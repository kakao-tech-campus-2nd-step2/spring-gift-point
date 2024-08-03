package gift.order.dto;

import gift.dto.ApiResponse;
import gift.dto.HttpResult;
import org.springframework.http.HttpStatus;

public class OrderResponse extends ApiResponse {

    public OrderResponse(HttpResult result, String message,
        HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
