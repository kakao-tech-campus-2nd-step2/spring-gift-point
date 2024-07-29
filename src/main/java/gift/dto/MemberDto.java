package gift.dto;

import jakarta.validation.constraints.*;

public record MemberDto(Long id,
                        @NotEmpty(message = "이메일 입력은 필수 입니다.")
                        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
                            message = "이메일 형식에 맞지 않습니다.")
                        String email,
                        @NotEmpty(message = "비밀번호 입력은 필수 입니다.")
                        @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
                        String password,
                        String accessToken) {

    public MemberDto(Long id, String email, String password, String accessToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }


    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
