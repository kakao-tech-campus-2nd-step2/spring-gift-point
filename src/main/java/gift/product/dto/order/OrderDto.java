package gift.product.dto.order;

public record OrderDto(
    Long optionId,
    int quantity,
    String message
) {

}
