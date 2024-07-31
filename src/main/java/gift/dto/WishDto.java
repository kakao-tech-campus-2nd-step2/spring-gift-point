package gift.dto;

import gift.vo.Wish;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 DTO")
public record WishDto (

        @Schema(description = "위시 ID")
        Long id,

        @Schema(description = "회원 정보 DTO")
        MemberDto memberDto,

        @Schema(description = "상품 응답 DTO")
        ProductResponseDto productResponseDto
) {
    public static WishDto toWishDto(Wish wish) {
        return new WishDto(
                wish.getId(),
                MemberDto.toMemberDto(wish.getMember()),
                ProductResponseDto.toProductResponseDto(wish.getProduct())
        );
    }
}
