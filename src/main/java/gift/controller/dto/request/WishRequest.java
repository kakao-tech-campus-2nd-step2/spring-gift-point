package gift.controller.dto.request;

import jakarta.validation.constraints.Min;

public class WishRequest {

    public record CreateWish(
            @Min(1)
            Long productId
    ) {
    }

    public record UpdateWish(
            @Min(1)
            Long id,
            @Min(1)
            Long productId,
            @Min(0)
            int productCount
    ) {
    }

}
