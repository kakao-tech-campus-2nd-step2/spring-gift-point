package gift.product.service.dto;

import gift.product.domain.ProductOption;

public record ProductOptionInfo(
        Long id,
        String name,
        Integer quantity
) {
    public static ProductOptionInfo from(ProductOption productOption) {
        return new ProductOptionInfo(productOption.getId(), productOption.getName(), productOption.getQuantity());
    }
}
