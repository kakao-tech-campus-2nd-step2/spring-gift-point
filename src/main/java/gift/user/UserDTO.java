package gift.user;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    @NotBlank(message = "이메일은 공백으로 둘 수 없습니다.")
    String email;
    @NotBlank(message = "비밀번호는 공백으로 둘 수 없습니다.")
    String password;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
