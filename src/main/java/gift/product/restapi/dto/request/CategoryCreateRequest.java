package gift.product.restapi.dto.request;

import gift.core.domain.product.ProductCategory;

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
