package gift.domain.controller.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.dto.response.CategoryResponse;
import gift.global.apiResponse.BasicApiResponse;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public class CategoryListApiResponse extends BasicApiResponse {

    private final List<CategoryResponse> categories;

    public CategoryListApiResponse(
        @JsonProperty(value = "status", required = true) HttpStatusCode status,
        @JsonProperty(value = "categories", required = true) List<CategoryResponse> categories
    ) {
        super(status);
        this.categories = categories;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }
}
