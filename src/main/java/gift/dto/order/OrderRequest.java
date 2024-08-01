package gift.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    public record Create(
            @NotNull
            Long productId,
            @NotNull
            Long optionId,
            @Min(1)
            @NotNull
            int quantity,
            @NotNull
            String message,
            @NotNull
            int point
    ) {

    }
}
