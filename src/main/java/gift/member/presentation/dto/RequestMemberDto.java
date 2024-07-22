package gift.member.presentation.dto;

import gift.member.business.dto.MemberRegisterDto;
import gift.member.business.dto.MemberLoginDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestMemberDto(
    @Email
    String email,
    @NotBlank
    String password
) {

    public MemberRegisterDto toMemberRegisterDto() {
        return new MemberRegisterDto(email, password);
    }

    public MemberLoginDto toMemberLoginDto() {
        return new MemberLoginDto(email, password);
    }
}
