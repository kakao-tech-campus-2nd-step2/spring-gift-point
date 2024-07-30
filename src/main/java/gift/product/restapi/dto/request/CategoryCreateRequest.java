package gift.product.restapi.dto.request;

import gift.core.domain.product.ProductCategory;

public record CategoryCreateRequest(
        String name
) {
    public ProductCategory toDomain() {
        return ProductCategory.of(name);
    }
}
