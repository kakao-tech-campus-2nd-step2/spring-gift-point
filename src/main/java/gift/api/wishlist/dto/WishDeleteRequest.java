package gift.api.wishlist.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WishDeleteRequest(
    @NotNull(message = "Product id is mandatory")
    @Positive(message = "Product id must be greater than zero")
    Long productId
) {

    public static WishDeleteRequest of(Long productId) {
        return new WishDeleteRequest(productId);
    }
}
