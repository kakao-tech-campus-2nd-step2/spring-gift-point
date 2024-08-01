package gift.controller.dto.request;

import jakarta.validation.constraints.Min;

public record OrderRequest(
        @Min(1)
        Long productId,
        @Min(1)
        Long optionId,
        @Min(1)
        int quantity,
        @Min(0)
        int point,
        String message
) {
}
