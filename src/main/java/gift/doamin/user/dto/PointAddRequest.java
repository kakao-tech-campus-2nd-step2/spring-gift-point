package gift.doamin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;

@Schema(description = "포인트 충전")
public class PointAddRequest {

    @Max(Integer.MAX_VALUE)
    @Schema(description = "충전할 포인트")
    Long point;

    public PointAddRequest() {
    }

    public Integer getPoint() {
        return point.intValue();
    }
}
