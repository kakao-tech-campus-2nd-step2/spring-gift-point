package gift.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull long optionId,
    @NotNull long quantity,
    @NotEmpty String message,
    @NotNull boolean usePoint,
    @NotNull long point
) {
}
