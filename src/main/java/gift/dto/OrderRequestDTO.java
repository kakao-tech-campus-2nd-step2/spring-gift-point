package gift.dto;

public record OrderRequestDTO(
    Long optionId,
    Long quantity,
    String message,
    String accessToken
) {

}
