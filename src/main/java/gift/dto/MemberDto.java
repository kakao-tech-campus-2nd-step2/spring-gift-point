package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberDto {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
