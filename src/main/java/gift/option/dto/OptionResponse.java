package gift.option.dto;

import gift.dto.ApiResponse;
import gift.dto.HttpResult;
import org.springframework.http.HttpStatus;

public class OptionResponse extends ApiResponse {

    public OptionResponse(HttpResult result, String message, HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
