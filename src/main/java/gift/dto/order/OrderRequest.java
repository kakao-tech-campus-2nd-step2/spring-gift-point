package gift.dto.order;

public record OrderRequest(Long optionId,
                           Integer quantity,
                           String message
) {
}
