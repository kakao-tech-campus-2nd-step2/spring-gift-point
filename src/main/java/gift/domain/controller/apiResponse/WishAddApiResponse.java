package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.WishResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class WishAddApiResponse extends BasicApiResponse {

    private final WishResponse wish;

    @JsonCreator
    public WishAddApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "wish", required = true) WishResponse wish
    ) {
        super(statusCode);
        this.wish = wish;
    }

    public WishResponse getWish() {
        return wish;
    }
}
