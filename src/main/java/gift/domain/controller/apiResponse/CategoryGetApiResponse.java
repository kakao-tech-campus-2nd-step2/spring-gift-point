package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.global.apiResponse.BasicApiResponse;
import org.springframework.http.HttpStatusCode;

public class CategoryGetApiResponse extends BasicApiResponse {

    private final CategoryResponse category;

    public CategoryGetApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode status,
        @JsonProperty(value = "category", required = true) CategoryResponse category
    ) {
        super(status);
        this.category = category;
    }

    public CategoryResponse getCategory() {
        return category;
    }
}
