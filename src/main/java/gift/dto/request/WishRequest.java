package gift.dto.request;

import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @NotNull
        Long productId,
        Integer quantity
) {
}
