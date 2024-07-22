package gift.wish.dto;

import gift.wish.domain.ProductCount;

import java.util.Objects;

public record WishUpdateRequestDto(ProductCount productCount) {
    public WishUpdateRequestDto {
        Objects.requireNonNull(productCount);
    }

}
