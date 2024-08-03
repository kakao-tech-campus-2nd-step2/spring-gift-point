package gift.dto.request;

public record OrderRequest(Long optionId, int quantity, String message, int points) {
}
