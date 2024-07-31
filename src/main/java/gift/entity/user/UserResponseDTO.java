package gift.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponseDTO {

    @Schema(description = "유저 이메일", nullable = false, example = "test@mail.com")
    private String email;
    @Schema(description = "유저 role", nullable = false, example = "USER")
    private String role;

    public UserResponseDTO(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
