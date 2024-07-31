package gift.api.category.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.category.domain.Category;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryResponse(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
) {
    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(),
                                    category.getName(),
                                    category.getColor(),
                                    category.getImageUrl(),
                                    category.getDescription());
    }
}
