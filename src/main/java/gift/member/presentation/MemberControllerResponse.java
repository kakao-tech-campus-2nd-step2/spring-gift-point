package gift.member.presentation;

import gift.member.application.MemberServiceResponse;

public record MemberControllerResponse(
        Long id,
        String email,
        String password
) {
    public static MemberControllerResponse from(MemberServiceResponse member) {
        return new MemberControllerResponse(member.id(), member.email(), member.password());
    }
}
