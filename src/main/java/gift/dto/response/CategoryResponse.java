package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.CategoryDto;

public class CategoryResponse {

    private CategoryDto category;

    @JsonCreator
    public CategoryResponse(
        @JsonProperty("category") CategoryDto category
    ) {
        this.category = category;
    }

    public CategoryDto getCategory() {
        return category;
    }
}
