package gift.model.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserPointChargeRequest {

    @Schema(description = "유저 이메일", nullable = false, example = "test@naver.com")
    private String email;
    @Schema(description = "유저 포인트", nullable = false, example = "1")
    private int point;

    public UserPointChargeRequest(String email, int point) {
        this.email = email;
        this.point = point;
    }

    public UserPointChargeRequest(User user) {
        this.point = user.getPoint();
    }

    public UserPointChargeRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
