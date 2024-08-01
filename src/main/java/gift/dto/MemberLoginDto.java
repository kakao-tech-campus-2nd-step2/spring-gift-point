package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class MemberLoginDto {

    @Email(message = "잘못된 이메일 형식입니다.")
    @NotEmpty(message = "이메일은 필수 입력사항입니다.")
    @Pattern(regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "올바른 이메일 형식을 입력해 주세요.")
    public final String email;

    @NotEmpty(message = "비밀번호는 필수 입력사항입니다.")
    public final String password;

    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
