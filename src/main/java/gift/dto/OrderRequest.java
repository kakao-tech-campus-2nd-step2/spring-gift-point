package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record OrderRequest(
        @NotNull @Min(1) Long optionId,
        @NotNull @Min(1) int quantity,
        String message
) {
    public OrderRequest {
        Objects.requireNonNull(optionId, "optionId cannot be null");
        Objects.requireNonNull(quantity, "quantity cannot be null");
    }
}
