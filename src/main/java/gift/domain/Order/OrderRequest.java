package gift.domain.Order;

public record OrderRequest(
        Long optionId,
        Long quantity,
        String message
) {
}
