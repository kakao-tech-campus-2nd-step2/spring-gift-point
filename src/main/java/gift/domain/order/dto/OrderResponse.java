package gift.domain.order.dto;

public record OrderResponse(
    Long id,
    Long optionId,
    int quantity,
    String orderDateTime,
    String message
) {

}
