package gift.wishlist.dto;

import gift.wishlist.entity.WishList;

public record WishListResDto(
        Long id,
        Long productId,
        Integer quantity
) {

    public WishListResDto(WishList wishList) {
        this(wishList.getId(), wishList.getProduct().getId(), wishList.getQuantity());
    }
}
