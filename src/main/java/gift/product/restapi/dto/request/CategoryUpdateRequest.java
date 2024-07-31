package gift.product.restapi.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.product.ProductCategory;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CategoryUpdateRequest(
        String name,
        String color,
        String imageUrl,
        String description
) {
    public ProductCategory toDomainWithId(Long id) {
        return new ProductCategory(id, name, color, imageUrl, description);
    }
}
