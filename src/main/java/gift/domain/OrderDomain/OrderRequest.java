package gift.domain.OrderDomain;

public record OrderRequest(
        Long optionId,
        Long quantity,
        String message
) {
}
