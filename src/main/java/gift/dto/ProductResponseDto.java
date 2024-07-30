package gift.dto;

import gift.vo.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 응답 DTO")
public record ProductResponseDto (

        @Schema(description = "상품 ID")
        Long id,

        @Schema(description = "카테고리 DTO")
        CategoryDto categoryDto,

        @Schema(description = "상품명")
        String name,

        @Schema(description = "상품 가격")
        Integer price,

        @Schema(description = "상품 이미지 URL")
        String imageUrl,

        @Schema(description = "옵션 리스트")
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
