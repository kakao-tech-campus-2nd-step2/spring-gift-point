package gift.api.wishlist.dto;

import gift.api.product.domain.Product;
import gift.api.wishlist.domain.Wish;

public record WishResponse(
    Product product,
    Integer quantity
) {
    public static WishResponse of(Wish wish) {
        return new WishResponse(wish.getProduct(),
                                wish.getQuantity());
    }
}
