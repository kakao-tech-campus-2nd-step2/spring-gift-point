package gift.dto;

import gift.vo.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답 DTO")
public record ProductResponseDto (

        @Schema(description = "상품 ID")
        Long id,

        @Schema(description = "상품명")
        String name,

        @Schema(description = "상품 가격")
        Integer price,

        @Schema(description = "상품 이미지 URL")
        String imageUrl,

        @Schema(description = "카테고리 정보 DTO")
        CategoryDto category

) {
    public static ProductResponseDto toProductResponseDto (Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            CategoryDto.fromCategory(product.getCategory())
        );
    }
}
