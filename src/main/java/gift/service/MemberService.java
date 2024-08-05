package gift.service;

import gift.domain.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public int getMemberPoints(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"))
            .getPoints();
    }

    public void addPoints(Long memberId, int points) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        Member updatedMember = member.updatePoints(member.getPoints() + points);
        memberRepository.save(updatedMember);
    }

    public void subtractPoints(Long memberId, int points) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        if (member.getPoints() < points) {
            throw new IllegalArgumentException("Insufficient points");
        }
        Member updatedMember = member.updatePoints(member.getPoints() - points);
        memberRepository.save(updatedMember);
    }
}
