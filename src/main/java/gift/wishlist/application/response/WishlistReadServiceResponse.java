package gift.wishlist.application.response;

import gift.wishlist.domain.Wishlist;

public record WishlistReadServiceResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistReadServiceResponse from(Wishlist wishlist) {
        return new WishlistReadServiceResponse(
                wishlist.getId(),
                wishlist.getMember().getId(),
                wishlist.getProduct().getId()
        );
    }

    public Long getId() {
        return id;
    }
}
