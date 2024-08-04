package gift.wishlist.presentation.response;

import gift.wishlist.application.response.WishlistReadServiceResponse;

public record WishlistReadResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistReadResponse from(WishlistReadServiceResponse wishlist) {
        return new WishlistReadResponse(
                wishlist.getId(),
                wishlist.memberId(),
                wishlist.productId()
        );
    }

    public Long getId() {
        return id;
    }
}
