package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.WishAddResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class WishAddApiResponse extends BasicApiResponse {

    private final WishAddResponse result;

    @JsonCreator
    public WishAddApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "result", required = true) WishAddResponse result
    ) {
        super(statusCode);
        this.result = result;
    }

    public WishAddResponse getResult() {
        return result;
    }
}
