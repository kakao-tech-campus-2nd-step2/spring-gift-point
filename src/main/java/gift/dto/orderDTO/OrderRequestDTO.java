package gift.dto.orderDTO;

public record OrderRequestDTO(
    Long optionId,
    Long quantity,
    String message,
    String accessToken
) {

}
