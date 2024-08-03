package gift.kakaomember.dto;

import gift.dto.ApiResponse;
import gift.dto.HttpResult;
import org.springframework.http.HttpStatus;

public class KakaoResponse extends ApiResponse {

    public KakaoResponse(HttpResult result, String message,
        HttpStatus httpStatus, Object data) {
        super(result, message, httpStatus, data);
    }
}
