package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MemberLoginDto {

    @Email
    @NotEmpty
    public final String email;

    @NotEmpty
    public final String password;

    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
