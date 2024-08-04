package gift.model.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserPointChargeDTO {

    @Schema(description = "유저 포인트", nullable = false, example = "1")
    private int point;

    public UserPointChargeDTO(int point) {
        this.point = point;
    }

    public UserPointChargeDTO(User user) {
        this.point = user.getPoint();
    }

    public UserPointChargeDTO() {
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
