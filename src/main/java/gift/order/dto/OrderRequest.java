package gift.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest (
        @NotNull
        Long optionId,
        @NotNull
        Integer quantity,
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        String message
) { }
