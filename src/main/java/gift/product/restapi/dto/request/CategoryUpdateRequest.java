package gift.product.restapi.dto.request;

import gift.core.domain.product.ProductCategory;

public record CategoryUpdateRequest(
        String name
) {
    public ProductCategory toDomainWithId(Long id) {
        return new ProductCategory(id, name);
    }
}
