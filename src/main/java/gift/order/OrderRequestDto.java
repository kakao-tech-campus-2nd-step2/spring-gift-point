package gift.order;

public record OrderRequestDto(Long optionId, int quantity, String message) {
}
