package gift.dto;

import jakarta.validation.constraints.Min;

public record PointResponse(
        @Min(0)
        int point
) {
}
