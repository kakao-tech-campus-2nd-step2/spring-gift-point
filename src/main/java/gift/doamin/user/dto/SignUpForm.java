package gift.doamin.user.dto;

import gift.doamin.user.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public class SignUpForm {

    @Schema(description = "이메일", example = "test@kakao.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank
    private String password;

    @Schema(description = "사용자 이름", maxLength = 15)
    @Size(min = 1, max = 15)
    @NotBlank
    private String name;

    @NotNull
    private UserRole role;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
}
