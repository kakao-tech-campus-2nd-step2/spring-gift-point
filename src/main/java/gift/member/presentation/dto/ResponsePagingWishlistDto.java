package gift.member.presentation.dto;

import gift.member.business.dto.WishlistPagingDto;
import java.util.List;

public record ResponsePagingWishlistDto(
    boolean hasNext,
    List<ResponseWishListDto> wishlistList) {

    public static ResponsePagingWishlistDto from(WishlistPagingDto wishListPagingDto) {
        return new ResponsePagingWishlistDto(
            wishListPagingDto.hasNext(),
            ResponseWishListDto.of(wishListPagingDto.wishlistList())
        );
    }
}
