package gift.user.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일은 필수 항목입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password,

        @Nullable
        String role
) {
    public LoginRequest(String email, String password) {
        this(email, password, "USER");
    }
}
