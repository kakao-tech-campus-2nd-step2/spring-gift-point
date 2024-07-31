package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class MemberRegisterApiResponse extends BasicApiResponse {

    private final String token;

    @JsonCreator
    public MemberRegisterApiResponse(
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
