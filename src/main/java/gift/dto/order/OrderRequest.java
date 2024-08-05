package gift.dto.order;

import jakarta.validation.constraints.*;

public class OrderRequest {
    public record Create(
            @NotNull
            Long productId,
            @NotNull
            Long optionId,
            @Min(1)
            @NotNull
            Integer quantity,
            @NotBlank
            String message,
            @NotNull
            Integer point
    ) {

    }
}
