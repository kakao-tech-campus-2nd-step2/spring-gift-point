package gift.product.application.dto.response;

import gift.product.service.dto.ProductOptionInfo;

public record ProductOptionResponse(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOptionResponse from(ProductOptionInfo productOptionInfo) {
        return new ProductOptionResponse(
                productOptionInfo.id(),
                productOptionInfo.name(),
                productOptionInfo.quantity()
        );
    }
}
