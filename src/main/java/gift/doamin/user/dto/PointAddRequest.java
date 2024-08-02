package gift.doamin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 충전")
public class PointAddRequest {

    @Schema(description = "충전할 포인트")
    Integer point;

    public PointAddRequest() {
    }

    public Integer getPoint() {
        return point;
    }
}
