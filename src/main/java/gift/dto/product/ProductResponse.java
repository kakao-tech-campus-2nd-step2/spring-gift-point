package gift.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답 데이터")
public record ProductResponse(
    @Schema(description = "상품 ID", example = "1")
    Long id,

    @Schema(description = "상품 이름", example = "Product1")
    String name,

    @Schema(description = "상품 가격", example = "1000")
    int price,

    @Schema(description = "상품 이미지 URL", example = "https://via.placeholder.com/150?text=product1")
    String imageUrl,

    @Schema(description = "카테고리 ID", example = "1")
    Long categoryId,

    @Schema(description = "카테고리 이름", example = "교환권")
    String categoryName
) {

}
