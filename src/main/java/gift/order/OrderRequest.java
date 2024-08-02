package gift.order;

public record OrderRequest(Long optionId,
                           Integer quantity,
                           String message,
                           boolean usePoint,
                           Integer point) {

}
