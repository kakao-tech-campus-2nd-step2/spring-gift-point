package gift.order.application.dto;

public record OrderRequestDto(Long optionId, int quantity, String message) {
}
