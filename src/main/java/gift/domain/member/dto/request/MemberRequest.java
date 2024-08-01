package gift.domain.member.dto.request;

import gift.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest (
    @Email
    String email,

    @NotBlank
    String password
)
{
    public Member toMember() {
        return new Member(this.email, this.password); // dto to entity
    }
}
