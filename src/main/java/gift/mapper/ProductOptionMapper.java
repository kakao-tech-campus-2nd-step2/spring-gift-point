package gift.mapper;

import gift.dto.ProductOptionResponseDto;
import gift.entity.ProductOption;

public class ProductOptionMapper {
    public static ProductOptionResponseDto toProductOptionResponseDto(ProductOption productOption) {
        return new ProductOptionResponseDto(
                productOption.getId(),
                productOption.getProduct().getName().getValue(),
                productOption.getOption().getName().getValue(),
                productOption.getQuantity()
        );
    }
}
