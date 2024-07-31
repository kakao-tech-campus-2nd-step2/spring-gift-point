package gift.wish.service.dto;

import gift.wish.domain.Wish;

public record WishInfo(
        Long id,
        Long productId,
        Integer amount
) {
    public static WishInfo from(Wish wish) {
        return new WishInfo(wish.getId(), wish.getProduct().getId(), wish.getAmount());
    }
}
