package gift.member.presentation.dto;

import jakarta.validation.constraints.Min;

public class PointRequest {

    public record Init(
        @Min(value = 10000, message = "포인트는 최소 10000 이상으로 충전해야 합니다.")
        Long point
    ) {
    }

}
