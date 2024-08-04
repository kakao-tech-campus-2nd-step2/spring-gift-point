package gift.wishlist.application.response;

import gift.wishlist.domain.Wishlist;

public record WishlistSaveServiceResponse(
        Long id,
        Long memberId,
        Long productId
) {
    public static WishlistSaveServiceResponse from(Wishlist wishlist) {
        return new WishlistSaveServiceResponse(
                wishlist.getId(),
                wishlist.getMember().getId(),
                wishlist.getProduct().getId()
        );
    }
}
