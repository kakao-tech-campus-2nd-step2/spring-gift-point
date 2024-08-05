package gift.member.application.response;

import gift.member.domain.Member;

public record MemberRegisterServiceResponse(
        Long id,
        String email
) {
    public static MemberRegisterServiceResponse from(Member member) {
        return new MemberRegisterServiceResponse(member.getId(), member.getEmail());
    }
}
