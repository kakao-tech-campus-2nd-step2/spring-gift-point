package gift.mapper;

import gift.dto.ProductResponseDto;
import gift.dto.WishResponseDto;
import gift.entity.Wish;

public class WishMapper {
    public static WishResponseDto toWishResponseDto(Wish wish, ProductResponseDto product) {
        return new WishResponseDto(wish.getId(), product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
