package gift.product.restapi.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.product.ProductCategory;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryCreateRequest(
        String name,
        String color,
        String imageUrl,
        String description
) {
    public ProductCategory toDomain() {
        return ProductCategory.of(name, color, imageUrl, description);
    }
}
