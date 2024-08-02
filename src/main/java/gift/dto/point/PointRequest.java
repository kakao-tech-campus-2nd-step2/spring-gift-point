package gift.dto.point;

import jakarta.validation.constraints.Positive;

public record PointRequest(
        @Positive(message = "포인트는 최소 1원 이상이어야 추가할 수 있습니다.")
        Integer point
) {
}
