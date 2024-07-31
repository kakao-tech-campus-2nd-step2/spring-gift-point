package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class MemberRegisterDto {

    @Email
    @NotEmpty
    public final String email;

    @NotEmpty
    public final String password;
    public MemberRegisterDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
