package gift.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest (
        @NotNull
        Long optionId,
        @NotNull
        Integer quantity,
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        String message,
        @NotNull
        @Min(value = 0, message = "0 이상의 값이 입력되어야 합니다.")
        Integer point
) { }
