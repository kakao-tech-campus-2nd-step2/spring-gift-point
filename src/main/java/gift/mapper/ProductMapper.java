package gift.mapper;

import gift.dto.CategoryResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.entity.ProductName;

public class ProductMapper {
    public static ProductResponseDto toProductResponseDTO(Product product) {
        CategoryResponseDto categoryResponseDto = CategoryMapper.toCategoryResponseDto(product.getCategory());
        return new ProductResponseDto(
                product.getId(),
                product.getName().getValue(),
                product.getPrice(),
                product.getImageUrl(),
                categoryResponseDto
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
