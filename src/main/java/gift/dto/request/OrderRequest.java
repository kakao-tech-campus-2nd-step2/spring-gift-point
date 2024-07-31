package gift.dto.request;

public record OrderRequest(
	Long optionId,
	Long quantity,
	String message
) {
}
