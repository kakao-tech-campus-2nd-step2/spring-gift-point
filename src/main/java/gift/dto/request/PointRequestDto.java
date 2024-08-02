package gift.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PointRequestDto(
        @NotNull(message = "포인트는 0이상이어야 합니다.")
        @Positive(message = "포인트는 0이상이어야 합니다.")
        Integer point
) {
}
