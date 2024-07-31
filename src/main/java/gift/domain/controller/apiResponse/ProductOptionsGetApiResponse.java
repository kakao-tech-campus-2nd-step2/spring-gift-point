package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.domain.dto.response.OptionResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class ProductOptionsGetApiResponse extends BasicApiResponse {

    private final List<OptionResponse> options;

    @JsonCreator
    public ProductOptionsGetApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "options", required = true) List<OptionResponse> options
    ) {
        super(statusCode);
        this.options = options;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }
}
