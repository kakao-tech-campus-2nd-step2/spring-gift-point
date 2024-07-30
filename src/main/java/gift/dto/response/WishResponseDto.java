package gift.dto.response;

import gift.domain.Wish;

public record WishResponseDto(
        Long id,
        ProductResponseDto productResponseDto,
        int count
) {
    public static WishResponseDto from(Wish wish) {
        return new WishResponseDto(wish.getId(), ProductResponseDto.from(wish.getProduct()), wish.getCount());
    }
}
