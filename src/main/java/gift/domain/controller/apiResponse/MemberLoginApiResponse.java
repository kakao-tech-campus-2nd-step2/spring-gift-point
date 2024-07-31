package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class MemberLoginApiResponse extends BasicApiResponse {

    private final String token;

    public MemberLoginApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "token", required = true) String token
    ) {
        super(statusCode);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
