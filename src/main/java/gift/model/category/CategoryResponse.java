package gift.model.category;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryResponse(Long id, String name, String color, @JsonProperty("image_url") String imageUrl, String description) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }
}
