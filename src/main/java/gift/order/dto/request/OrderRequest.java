package gift.order.dto.request;

public record OrderRequest(
    Long optionId,
    Integer quantity,
    String message
) {

}
