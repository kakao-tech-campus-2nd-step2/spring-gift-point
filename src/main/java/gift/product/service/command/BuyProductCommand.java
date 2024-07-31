package gift.product.service.command;

public record BuyProductCommand(
        Long productId,
        Long optionId,
        Integer quantity,
        String message,
        Long memberId
) {
}
