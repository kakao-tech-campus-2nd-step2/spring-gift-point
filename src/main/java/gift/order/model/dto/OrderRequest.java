package gift.order.model.dto;

public record OrderRequest(Long optionId,
                           Integer quantity,
                           String message
) {
}
