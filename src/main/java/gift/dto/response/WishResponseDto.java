package gift.dto.response;

import gift.domain.Wish;

public record WishResponseDto(
        Long id,
        Long productId,
        int count
) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(wish.getId(), wish.getProduct().getId(), wish.getCount());
    }
}