package gift.doamin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "포인트 조회")
public class PointResponse {

    @Schema(description = "보유 포인트")
    Integer point;

    public PointResponse(Integer point) {
        this.point = point;
    }

    public Integer getPoint() {
        return point;
    }
}
