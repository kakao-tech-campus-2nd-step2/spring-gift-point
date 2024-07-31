package gift.api.wishlist.dto;

import gift.api.wishlist.domain.Wish;

public record WishResponse(
    Long id,
    Integer quantity
) {
    public static WishResponse of(Wish wish) {
        return new WishResponse(wish.getId(),
                                wish.getQuantity());
    }
}
