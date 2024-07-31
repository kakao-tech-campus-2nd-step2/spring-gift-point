package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OauthTokenResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class OauthTokenApiResponse extends BasicApiResponse {

    private final OauthTokenResponse result;

    public OauthTokenApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "result", required = true) OauthTokenResponse result) {
        super(statusCode);
        this.result = result;
    }

    public OauthTokenResponse getResult() {
        return result;
    }
}
