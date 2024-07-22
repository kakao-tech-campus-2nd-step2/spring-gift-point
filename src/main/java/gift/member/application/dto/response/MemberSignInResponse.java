package gift.member.application.dto.response;

import gift.member.service.dto.MemberSignInInfo;
import gift.member.service.dto.MemberSignupInfo;

public record MemberSignInResponse(
        String token
) {
    public static MemberSignInResponse from(MemberSignInInfo memberSignInInfo) {
        return new MemberSignInResponse(memberSignInInfo.token());
    }

    public static MemberSignInResponse from(MemberSignupInfo memberSignupInfo) {
        return new MemberSignInResponse(memberSignupInfo.token());
    }
}
