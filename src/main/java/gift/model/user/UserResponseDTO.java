package gift.model.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDTO {

    @Schema(description = "유저 이메일", nullable = false, example = "test@mail.com")
    private String email;
    @Schema(description = "유저 포인트", nullable = false, example = "1")
    private int point;

    public UserResponseDTO(User user) {
        this.email = user.getEmail();
        this.point = user.getPoint();
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
