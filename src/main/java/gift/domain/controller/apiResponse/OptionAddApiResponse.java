package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OptionResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class OptionAddApiResponse extends BasicApiResponse {

    private final OptionResponse option;

    @JsonCreator
    public OptionAddApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "option", required = true) OptionResponse option
    ) {
        super(statusCode);
        this.option = option;
    }

    public OptionResponse getOption() {
        return option;
    }
}
