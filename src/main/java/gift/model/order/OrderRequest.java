package gift.model.order;

public record OrderRequest(
    Long optionId,
    Long productId,
    int quantity,
    String message
) {

}
