package gift.domain.dto.request;

import gift.domain.entity.Wish;
import jakarta.validation.constraints.NotNull;

public record WishRequest(@NotNull Long productId, @NotNull Long quantity) {

    public static WishRequest of(Wish wish) {
        return new WishRequest(wish.getProduct().getId(), wish.getQuantity());
    }
}
