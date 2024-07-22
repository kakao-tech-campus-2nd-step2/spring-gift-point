package gift.product.application.dto.request;

import gift.product.service.command.BuyProductCommand;
import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull
        Long optionId,
        @NotNull
        Integer quantity,
        @NotNull
        String message
) {
    public BuyProductCommand toCommand(Long memberId, Long productId) {
        return new BuyProductCommand(productId, optionId, quantity, message, memberId);
    }
}
