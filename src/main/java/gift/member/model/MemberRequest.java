package gift.member.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
    @Email
    String email,
    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password,
    String name,
    String role) {

    public Member toEntity() {
        return new Member(
            email,
            password,
            name,
            role
        );
    }
}
