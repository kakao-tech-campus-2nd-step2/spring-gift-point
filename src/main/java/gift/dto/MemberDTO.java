package gift.dto;

import gift.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberDTO(
    @NotBlank
    @Email(message = "이메일 양식에 맞지 않습니다.")
    String email,

    @NotBlank
    String password
) {

    public Member toEntity() {
        return new Member(email, password);
    }
}
