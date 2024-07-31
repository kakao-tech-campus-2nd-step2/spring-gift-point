package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class CategoryAddApiResponse extends BasicApiResponse {

    private final CategoryResponse createdCategory;

    @JsonCreator
    public CategoryAddApiResponse(
        @JsonProperty(value = "status") HttpStatusCode statusCode,
        @JsonProperty(value = "created-category") CategoryResponse createdCategory
    ) {
        super(statusCode);
        this.createdCategory = createdCategory;
    }

    public CategoryResponse getCreatedCategory() {
        return createdCategory;
    }
}
