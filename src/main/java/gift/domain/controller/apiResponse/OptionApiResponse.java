package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class OptionApiResponse extends BasicApiResponse {

    private final OptionDetailedResponse option;

    @JsonCreator
    public OptionApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "option", required = true) OptionDetailedResponse option
    ) {
        super(statusCode);
        this.option = option;
    }

    public OptionDetailedResponse getOption() {
        return option;
    }
}
