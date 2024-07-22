package gift.member.persistence.repository;

import gift.global.exception.ErrorCode;
import gift.global.exception.custrom.NotFoundException;
import gift.member.persistence.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository{
    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryImpl(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Long saveMember(Member member) {
        return memberJpaRepository.save(member).getId();
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Member with email " + email + " not found")
            );
    }

    @Override
    public Member getMemberById(Long id) {
        return memberJpaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(
                ErrorCode.DB_NOT_FOUND,
                "Member with id " + id + " not found")
            );
    }

    @Override
    public Member getReferencedMember(Long memberId) {
        return memberJpaRepository.getReferenceById(memberId);
    }
}
