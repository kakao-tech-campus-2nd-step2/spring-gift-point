package gift.wish.dto;

import gift.dto.ApiResponse;
import gift.model.HttpResult;
import org.springframework.http.HttpStatus;

public class WishResponse extends ApiResponse {

    public WishResponse(HttpResult result, String message, HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
