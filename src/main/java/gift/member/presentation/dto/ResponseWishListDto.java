package gift.member.presentation.dto;

import gift.member.business.dto.WishListDto;
import java.util.List;

public record ResponseWishListDto(
    Long id,
    Long productId,
    String productName,
    Integer price,
    String imageUrl
) {

    public static ResponseWishListDto from(WishListDto wishListDto) {
        return new ResponseWishListDto(
            wishListDto.id(),
            wishListDto.productId(),
            wishListDto.productName(),
            wishListDto.price(),
            wishListDto.imageUrl()
        );
    }

    public static List<ResponseWishListDto> of(List<WishListDto> wishListDtos) {
        return wishListDtos.stream()
            .map(ResponseWishListDto::from)
            .toList();
    }
}
