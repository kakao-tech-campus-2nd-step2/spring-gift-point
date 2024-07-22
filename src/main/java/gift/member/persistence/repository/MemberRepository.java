package gift.member.persistence.repository;

import gift.member.persistence.entity.Member;

public interface MemberRepository {

    Long saveMember(Member member);

    Member getMemberByEmail(String email);

    Member getMemberById(Long id);

    Member getReferencedMember(Long memberId);
}
