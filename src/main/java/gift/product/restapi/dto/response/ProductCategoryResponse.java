package gift.product.restapi.dto.response;

import gift.core.domain.product.ProductCategory;

public record ProductCategoryResponse(
        Long id,
        String name
) {
    public static ProductCategoryResponse of(ProductCategory category) {
        return new ProductCategoryResponse(
                category.id(),
                category.name()
        );
    }
}
