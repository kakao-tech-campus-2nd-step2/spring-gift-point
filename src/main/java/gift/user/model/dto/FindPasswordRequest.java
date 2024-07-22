package gift.user.model.dto;

import jakarta.validation.constraints.NotBlank;

public class FindPasswordRequest {
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
