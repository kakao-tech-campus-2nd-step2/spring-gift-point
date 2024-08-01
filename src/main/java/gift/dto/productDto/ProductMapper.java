package gift.dto.productDto;

import gift.model.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponseDto toProductResponseDto(Product product) {
        return new ProductResponseDtoBuilder()
                .id(product.getId())
                .name(product.getName().getName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory().getId())
                .build();
    }
}
