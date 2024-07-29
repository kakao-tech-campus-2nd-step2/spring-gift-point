package gift.dto;

import jakarta.validation.constraints.Min;

public record OptionSubtractRequest(
        Long id,
        @Min(1)
        Long quantity
) {
}
