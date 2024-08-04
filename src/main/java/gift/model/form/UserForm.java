package gift.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 등록/로그인 폼")
public class UserForm {

    @Schema(description = "사용자 이메일", example = "user@example.com")
    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Size(min = 3, max = 30, message = "이메일의 길이는 3 이상 ~ 30 이하입니다.")
    private final String email;

    @Schema(description = "사용자 비밀번호", example = "password123")
    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 3, max = 15, message = "비밀번호의 길이는 3 이상 ~ 15 이하입니다.")
    private final String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
