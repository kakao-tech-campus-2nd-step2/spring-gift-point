package gift.dto.order;

public record OrderResponseDTO(Long optionId,
                               Long quantity,
                               String orderDateTime,
                               String message) {
}
