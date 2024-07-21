package gift.service;

import gift.entity.Member;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public Member updateMember(Long memberId, String email, String password) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));

        member.update(email, password);

        return memberRepository.save(member);
    }
}
