package gift.category;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryResponse(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
) {

    public CategoryResponse(Category category) {
        this(category.getId(), category.getName(), category.getColor(), category.getImageUrl(),
            category.getDescription());
    }

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(),
            category.getName(), category.getColor(), category.getImageUrl(),
            category.getDescription()
        );
    }
}
