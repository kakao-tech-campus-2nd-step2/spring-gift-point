package gift.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Category;

public record CategoryResponse(
    Long id,
    String name,
    String color,
    @JsonProperty(value = "image_url")
    String imageUrl,
    String description
) {

    private CategoryResponse(Category category) {
        this(category.getId(), category.getName(),
            category.getColor(), category.getImageUrl(), category.getDescription());
    }

    public static CategoryResponse createCategoryResponse(Category category) {
        return new CategoryResponse(category);
    }
}
