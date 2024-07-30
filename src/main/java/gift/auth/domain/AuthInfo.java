package gift.auth.domain;

import gift.member.domain.Member;
import gift.member.domain.MemberType;

public record AuthInfo(Long memberId, MemberType memberType) {
    public AuthInfo(Member member) {
        this(member.getId(), member.getMemberType());
    }
}
