package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class CategoryAddApiResponse extends BasicApiResponse {

    private final CategoryResponse category;

    @JsonCreator
    public CategoryAddApiResponse(
        @JsonProperty(value = "status") HttpStatusCode statusCode,
        @JsonProperty(value = "category") CategoryResponse category
    ) {
        super(statusCode);
        this.category = category;
    }

    public CategoryResponse getCategory() {
        return category;
    }
}
