package gift.wishlist.util;

import gift.product.util.OptionMapper;
import gift.wishlist.dto.WishResponse;
import gift.wishlist.entity.Wish;

public class WishMapper {

    public static WishResponse toResponseDto(Wish wish) {
        return new WishResponse(
                wish.getId(),
                OptionMapper.toWishResponseDto(wish.getOption())
        );
    }

}
