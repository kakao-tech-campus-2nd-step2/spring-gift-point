package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberDTO {

    private Long id;

    @NotBlank(message = "이메일은 1글자 이상 입력하세요")
    @Email(message = "이메일 형식을 지켜야 합니다")
    private String email;

    @NotBlank(message = "비밀번호는 1글자 이상 입력하세요")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
