package gift.dto;

import gift.vo.Wish;

public record WishDto (
        Long id,
        MemberDto memberDto,
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
