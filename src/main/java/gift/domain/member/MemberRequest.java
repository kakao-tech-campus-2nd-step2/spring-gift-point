package gift.domain.member;

import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
    @NotBlank(message = "아이디가 입력되지 않았습니다")
    String email,

    @NotBlank(message = "비밀번호가 입력되지 않았습니다")
    String password
) {

    public Member toMember() {
        return new Member(MemberType.EMAIL, email, password);
    }
}
