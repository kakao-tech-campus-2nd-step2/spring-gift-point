package gift.domain.wishlist.dto;

import gift.domain.wishlist.entity.WishItem;

public record WishItemResponseDto(
    Long id,
    Long memberId,
    Long productId
) {
    public static WishItemResponseDto from(WishItem wishItem) {
        return new WishItemResponseDto(
            wishItem.getId(),
            wishItem.getMemberId(),
            wishItem.getProductId()
        );
    }
}
