package gift.wishlist.dto;

import gift.product.dto.ProductResDto;
import gift.wishlist.entity.WishList;

public record WishListResDto(
        Long id,
        ProductResDto product
) {

    public WishListResDto(WishList wishList) {
        this(wishList.getId(), new ProductResDto(wishList.getProduct()));
    }
}
