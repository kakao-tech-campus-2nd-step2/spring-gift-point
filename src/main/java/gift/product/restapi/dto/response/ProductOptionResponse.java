package gift.product.restapi.dto.response;

import gift.core.domain.product.ProductOption;

public record ProductOptionResponse(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
                option.id(),
                option.name(),
                option.quantity()
        );
    }
}
