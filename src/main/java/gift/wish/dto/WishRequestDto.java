package gift.wish.dto;

import gift.wish.domain.ProductCount;

import java.util.Objects;

public record WishRequestDto(Long productId, ProductCount productCount) {
    public WishRequestDto {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(productCount);
    }

    public WishServiceDto toWishServiceDto(Long memberId) {
        return new WishServiceDto(null, memberId, productId, productCount);
    }

    public WishServiceDto toWishServiceDto(Long id, Long memberId) {
        return new WishServiceDto(id, memberId, productId, productCount);
    }
}
