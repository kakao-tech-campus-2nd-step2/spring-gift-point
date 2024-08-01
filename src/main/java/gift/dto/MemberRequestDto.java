package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequestDto {
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수항목입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private final String password;

    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
