package gift.api.member.dao;

import gift.api.member.domain.Member;
import gift.api.member.repository.MemberRepository;
import gift.global.exception.NoSuchEntityException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final MemberRepository memberRepository;

    public MemberDao(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchEntityException("member"));
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("member"));
    }

    public Long saveMember(Member member) {
        return memberRepository.save(member).getId();
    }

    public boolean hasMemberByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean hasMemberByEmailAndPassword(String email, String password) {
        return memberRepository.existsByEmailAndPassword(email, password);
    }
}
