package gift.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;

public record PointResponseDto(
    int point
) {

}
