package gift.member.dto;

import gift.dto.ApiResponse;
import gift.model.HttpResult;
import org.springframework.http.HttpStatus;

public class MemberResponse extends ApiResponse {
    public MemberResponse(HttpResult result, String message, HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
