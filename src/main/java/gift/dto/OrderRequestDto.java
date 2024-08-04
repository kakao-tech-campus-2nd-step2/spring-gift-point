package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderRequestDto(
    @Min(1)
    Long productId,
    @Min(1)
    Long optionId,
    @Min(1)
    @Max(99999999)
    Integer quantity,
    @NotBlank
    String message,
    Integer point
) {
}
