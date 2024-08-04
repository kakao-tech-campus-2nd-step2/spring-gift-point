package gift.product.dto.auth;

import jakarta.validation.constraints.Min;

public record PointRequest(
    @Min(value = 1, message = "추가 또는 차감할 포인트는 최소 1 이상이어야 합니다.")
    int point
) {

}
