package gift.dto;

import gift.vo.Product;

import java.util.List;

public record ProductResponseDto (
        Long id,
        CategoryDto categoryDto,
        String name,
        Integer price,
        String imageUrl,
        List<OptionResponseDto> options
) {
    public static ProductResponseDto toProductResponseDto (Product product) {
        return new ProductResponseDto(
            product.getId(),
            CategoryDto.fromCategory(product.getCategory()),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getOptions().stream().map(OptionResponseDto::toOptionResponseDto).toList()
        );
    }
}
