package gift.dto;

import jakarta.validation.constraints.Min;

public record PointRequest(
        @Min(0)
        int point
) {
}
