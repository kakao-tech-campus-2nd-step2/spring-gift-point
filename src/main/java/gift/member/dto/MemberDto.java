package gift.member.dto;

import gift.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDto(
    @NotBlank
    @Email(message = "이메일 양식에 맞지 않습니다.")
    String email,

    @NotBlank
    String password,

    int point
) {

    public Member toEntity() {
        return new Member(email, password, point);
    }
}
