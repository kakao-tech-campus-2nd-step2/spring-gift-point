package gift.dto.order;

public record OrderRequest(
    Long optionId,
    int quantity,
    String message
) {

}
