package gift.dto;

public record OrderResponseDTO(
    Long id,
    Long productId,
    Long optionId,
    String orderDateTime,
    Long quantity,
    String message
) {

}
