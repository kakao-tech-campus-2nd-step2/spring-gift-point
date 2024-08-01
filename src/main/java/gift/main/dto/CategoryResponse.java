package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.main.entity.Category;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryResponse(long id,
                               String name,
                               String color,
                               String imageUrl,
                               String description
) {
    public CategoryResponse(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
        );
    }
}
