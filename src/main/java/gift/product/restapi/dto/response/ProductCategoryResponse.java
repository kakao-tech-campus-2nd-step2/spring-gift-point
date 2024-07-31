package gift.product.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.core.domain.product.ProductCategory;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductCategoryResponse(
        Long id,
        String name,
        String color,
        String imageUrl,
        String description
) {
    public static ProductCategoryResponse of(ProductCategory category) {
        return new ProductCategoryResponse(
                category.id(),
                category.name(),
                category.color(),
                category.imageUrl(),
                category.description()
        );
    }
}
