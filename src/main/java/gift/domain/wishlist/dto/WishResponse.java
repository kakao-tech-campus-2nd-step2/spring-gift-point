package gift.domain.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 응답 Dto")
public record WishResponse(
    @Schema(description = "상품 id")
    Long id,
    @Schema(description = "상품 이름")
    String name,
    @Schema(description = "상품 가격")
    int price,
    @Schema(description = "상품 이미지 Url")
    String imageUrl
    ) {

}
