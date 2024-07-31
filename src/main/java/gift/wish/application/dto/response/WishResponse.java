package gift.wish.application.dto.response;

import gift.wish.service.dto.WishInfo;

public record WishResponse(
        Long id,
        Long productId,
        int amount
) {
    public static WishResponse from(WishInfo wishInfo) {
        return new WishResponse(wishInfo.id(), wishInfo.productId(), wishInfo.amount());
    }
}
