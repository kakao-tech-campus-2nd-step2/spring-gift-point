package gift.dto.point;

import jakarta.validation.constraints.NotNull;

public record PointRequestDTO(@NotNull Long point) {
}
