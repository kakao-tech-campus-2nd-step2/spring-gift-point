package gift.dto.orderDTO;

public record OrderResponseDTO(
    Long id,
    Long productId,
    Long optionId,
    String orderDateTime,
    Long quantity,
    String message
) {

}
