package gift.order.application.dto;

public record OrderRequestDto(Long optionId, int quantity, int point, String message) {
}
