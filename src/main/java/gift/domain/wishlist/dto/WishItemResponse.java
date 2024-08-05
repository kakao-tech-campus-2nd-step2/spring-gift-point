package gift.domain.wishlist.dto;

import gift.domain.wishlist.entity.WishItem;

public record WishItemResponse(
    Long id,
    Long memberId,
    Long productId
) {
    public static WishItemResponse from(WishItem wishItem) {
        return new WishItemResponse(
            wishItem.getId(),
            wishItem.getMemberId(),
            wishItem.getProductId()
        );
    }
}
