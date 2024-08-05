package gift.domain.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointRequestResponse(
    @NotNull(message = "충전할 포인트가 입력되지 않았습니다.")
    @Min(value = 10000, message = "충천할 포인트는 최소 10000원 이상이어야 합니다.")
    Integer point
) {

}
