package gift.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDTO {

    @Schema(description = "유저 이메일", nullable = false, example = "test@mail.com")
    private String email;

    public UserResponseDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
