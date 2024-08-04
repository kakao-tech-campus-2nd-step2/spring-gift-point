package gift.wishlist.application;

import gift.wishlist.domain.Wishlist;

public record WishlistServiceResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistServiceResponse from(Wishlist wishlist) {
        return new WishlistServiceResponse(
                wishlist.getId(),
                wishlist.getMember().getId(),
                wishlist.getProduct().getId()
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
