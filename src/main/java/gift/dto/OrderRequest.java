package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @Min(1)
        Long optionId,
        @Min(1)
        @Max(99999999)
        Long quantity,
        @NotBlank
        String message,
        int usedPoint
) {
}
