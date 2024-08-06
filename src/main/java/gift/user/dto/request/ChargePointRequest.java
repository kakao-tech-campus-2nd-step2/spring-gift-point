package gift.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChargePointRequest(
    @NotNull Long userId,
    @Positive Integer point
) {

}
