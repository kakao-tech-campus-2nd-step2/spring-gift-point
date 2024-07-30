package gift.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpReqeust {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 4, message = "비밀번호는 4자리 이상 입력해주세요.")
    String password;

    // Getter, Setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
