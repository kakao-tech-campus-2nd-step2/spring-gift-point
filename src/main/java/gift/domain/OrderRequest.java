package gift.domain;

public record OrderRequest(
        Long optionId,
        Long quantity,
        String message
) {
}
