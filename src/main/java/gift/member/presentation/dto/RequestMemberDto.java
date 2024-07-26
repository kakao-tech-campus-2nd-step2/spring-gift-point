package gift.member.presentation.dto;

import gift.member.business.dto.MemberIn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestMemberDto(
    @Email
    String email,
    @NotBlank
    String password
) {

    public MemberIn.Register toMemberRegisterDto() {
        return new MemberIn.Register(email, password);
    }

    public MemberIn.Login toMemberLoginDto() {
        return new MemberIn.Login(email, password);
    }
}
