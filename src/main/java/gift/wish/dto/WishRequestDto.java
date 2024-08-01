package gift.wish.dto;

import gift.wish.domain.ProductCount;

import java.util.Objects;

public record WishRequestDto(Long productId) {
    public WishRequestDto {
        Objects.requireNonNull(productId);;
    }

    public WishServiceDto toWishServiceDto(Long memberId) {
        return new WishServiceDto(null, memberId, productId);
    }

    public WishServiceDto toWishServiceDto(Long id, Long memberId) {
        return new WishServiceDto(id, memberId, productId);
    }
}
