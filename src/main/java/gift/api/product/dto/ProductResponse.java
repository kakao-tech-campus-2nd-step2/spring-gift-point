package gift.api.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.category.domain.Category;
import gift.api.product.domain.Product;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductResponse(
    Long id,
    Category category,
    String name,
    Integer price,
    String imageUrl
) {
    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(),
                                product.getCategory(),
                                product.getName(),
                                product.getPrice(),
                                product.getImageUrl());
    }
}
