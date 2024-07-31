package gift.api.category.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.category.domain.Category;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryRequest(
   String name,
   String color,
   String imageUrl,
   String description
) {

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
