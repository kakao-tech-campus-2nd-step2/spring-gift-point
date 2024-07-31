package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class OptionListApiResponse extends BasicApiResponse {

    private final List<OptionDetailedResponse> options;

    @JsonCreator
    public OptionListApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode statusCode,
        @JsonProperty(value = "options", required = true) List<OptionDetailedResponse> options
    ) {
        super(statusCode);
        this.options = options;
    }

    public List<OptionDetailedResponse> getOptions() {
        return options;
    }
}
