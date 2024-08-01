package gift.controller.dto.request;

import jakarta.validation.constraints.Min;

public record PointUpdateRequest(
        @Min(0)
        int depositPoint) {
}
