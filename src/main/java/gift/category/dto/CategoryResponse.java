package gift.category.dto;

import gift.dto.ApiResponse;
import gift.dto.HttpResult;
import org.springframework.http.HttpStatus;

public class CategoryResponse extends ApiResponse {

    public CategoryResponse(HttpResult result, String message, HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
