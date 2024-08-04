package gift.dto.user;

import gift.validation.constraint.EmailConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserRequestDTO {
    @EmailConstraint
    @Length(min = 1, max = 50)
    @NotNull
    @Schema(description = "유저 이메일", nullable = false, example = "test@mail.com")
    private String email;
    @Length(min = 1, max = 50)
    @NotNull
    @Schema(description = "유저 비밀번호", nullable = false, example = "pa_sSWo_R_d")
    private String password;


    public UserRequestDTO() {
    }

    public UserRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
