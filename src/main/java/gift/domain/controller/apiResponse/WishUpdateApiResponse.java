package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.WishResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class WishUpdateApiResponse extends BasicApiResponse {

    private final WishResponse result;

    public WishUpdateApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "result", required = true) WishResponse result
    ) {
        super(statusCode);
        this.result = result;
    }

    public WishResponse getResult() {
        return result;
    }
}
