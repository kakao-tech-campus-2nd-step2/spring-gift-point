package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequestDto(
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email,
        @NotBlank(message = "이름은 필수 항목입니다.")
        String name,
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password) {
    public MemberRegisterRequestDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String password() {
        return password;
    }
}
