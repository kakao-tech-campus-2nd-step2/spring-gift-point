package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @Email(message = "유효한 이메일 주소여야 합니다.")
    @NotBlank(message = "이메일은 빈 칸일 수 없습니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 빈 칸일 수 없습니다.")
    private final String password;

    public UserLoginDto(String email, String password) {
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
