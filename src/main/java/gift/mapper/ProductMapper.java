package gift.mapper;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.ProductName;

public class ProductMapper {
    public static ProductResponseDto toProductResponseDTO(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName().getValue(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId()
        );
    }

    public static Product toProduct(ProductRequestDto productRequestDto, Category category) {
        return new Product(
                new ProductName(productRequestDto.getName()),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl(),
                category
        );
    }
}
