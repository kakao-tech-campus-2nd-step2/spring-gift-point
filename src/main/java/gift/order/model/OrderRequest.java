package gift.order.model;

public record OrderRequest(Long optionId,
                           Integer quantity,
                           String message,
                           boolean usePoint,
                           Integer point) {

}
