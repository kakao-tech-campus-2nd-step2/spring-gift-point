package gift.domain.wishlist;

import gift.domain.option.Option;

public record WishListResponse(
    Long id,
    Option option
) {

    public WishListResponse(WishList wishList) {
        this(wishList.getId(), wishList.getOption());
    }
}
