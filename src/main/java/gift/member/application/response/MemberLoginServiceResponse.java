package gift.member.application.response;

import gift.member.domain.Member;

public record MemberLoginServiceResponse(
        Long id,
        String email
) {
    public static MemberLoginServiceResponse from(Member newMember) {
        return new MemberLoginServiceResponse(newMember.getId(), newMember.getEmail());
    }
}
