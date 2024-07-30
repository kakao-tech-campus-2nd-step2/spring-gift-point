package gift.domain.wishlist.dto;

import gift.domain.product.dto.ProductReadAllResponse;
import gift.domain.user.dto.UserResponse;
import gift.domain.wishlist.entity.WishItem;

public record WishItemResponseDto(
    Long id,
    UserResponse user,
    ProductReadAllResponse product
) {
    public static WishItemResponseDto from(WishItem wishItem) {
        return new WishItemResponseDto(
            wishItem.getId(),
            UserResponse.from(wishItem.getUser()),
            ProductReadAllResponse.from(wishItem.getProduct())
        );
    }
}
