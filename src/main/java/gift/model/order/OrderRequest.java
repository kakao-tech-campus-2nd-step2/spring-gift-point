package gift.model.order;

public record OrderRequest(
    Long userId,
    Long optionId,
    Long productId,
    int quantity,
    String message
) {

}
