package gift.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답 DTO")
public record ProductResponse(
    @Schema(description = "상품 Id")
    Long id,

    @Schema(description = "상품 Id")
    String name,

    @Schema(description = "상품 가격")
    int price,

    @Schema(description = "상품 이미지 Url")
    String imageUrl,

    @Schema(description = "카테고리 Id")
    Long categoryId
) {
}
