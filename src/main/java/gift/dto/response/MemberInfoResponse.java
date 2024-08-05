package gift.dto.response;

import gift.entity.Member;

public record MemberInfoResponse(
        Long id,
        String email,
        int point
) {
    public static MemberInfoResponse fromMember(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getPointBalance()
        );
    }
}
