package gift.wish.application.dto.request;

import gift.wish.service.command.WishCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishRequest(
        @NotNull
        Long productId,
        @NotNull
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        Integer amount
) {
    public WishCommand toWishParam(final Long userId) {
        return new WishCommand(productId, userId, amount);
    }
}
