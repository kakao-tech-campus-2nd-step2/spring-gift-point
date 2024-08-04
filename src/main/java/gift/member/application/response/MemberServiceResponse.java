package gift.member.application.response;

import gift.member.domain.Member;

public record MemberServiceResponse(
        Long id,
        String email,
        String password
) {
    public static MemberServiceResponse from(Member member) {
        return new MemberServiceResponse(member.getId(), member.getEmail(), member.getPassword());
    }
}
