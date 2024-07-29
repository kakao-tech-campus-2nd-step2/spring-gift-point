package gift.user;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "이메일을 입력하세요.")
    String email;
    @NotBlank(message = "비밀번호를 입력하세요.")
    String password;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginDTO() {
    }

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
