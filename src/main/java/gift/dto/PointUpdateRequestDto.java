package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record PointUpdateRequestDto (@Min(1)
                                     Long memberId,
                                     @Positive(message = "포인트는 양수여야 합니다.")
                                     int pointsToAdd) {

}
