package gift.dto;

import gift.vo.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 응답 객체 DTO")
public record WishResponseDto(

        @Schema(description = "위시 ID")
        Long wishId,

        @Schema(description = "상품 ID")
        Long productId,

        @Schema(description = "상품명")
        String productName,

        @Schema(description = "상품 이미지 url")
        String productImageUrl,

        @Schema(description = "카테고리 응답 객체 DTO")
        CategoryDto category

) {
    public static WishResponseDto toWishDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getProduct().getId(),
            wish.getProduct().getName(),
            wish.getProduct().getImageUrl(),
            CategoryDto.fromCategory(wish.getProduct().getCategory())
        );
    }
}
