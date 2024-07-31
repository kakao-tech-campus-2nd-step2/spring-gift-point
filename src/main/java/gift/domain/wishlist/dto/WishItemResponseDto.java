package gift.domain.wishlist.dto;

import gift.domain.member.dto.MemberResponse;
import gift.domain.product.dto.ProductReadAllResponse;
import gift.domain.wishlist.entity.WishItem;

public record WishItemResponseDto(
    Long id,
    MemberResponse member,
    ProductReadAllResponse product
) {
    public static WishItemResponseDto from(WishItem wishItem) {
        return new WishItemResponseDto(
            wishItem.getId(),
            MemberResponse.from(wishItem.getMember()),
            ProductReadAllResponse.from(wishItem.getProduct())
        );
    }
}
