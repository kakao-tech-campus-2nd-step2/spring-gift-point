package gift.wish.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateWishRequest(
    @NotNull Long productId
) {

}
