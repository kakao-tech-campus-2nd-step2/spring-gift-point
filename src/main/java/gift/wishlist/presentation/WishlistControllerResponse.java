package gift.wishlist.presentation;

import gift.wishlist.application.WishlistServiceResponse;

public record WishlistControllerResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistControllerResponse from(WishlistServiceResponse wishlist) {
        return new WishlistControllerResponse(
                wishlist.getId(),
                wishlist.getMemberId(),
                wishlist.getProductId()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }
}
