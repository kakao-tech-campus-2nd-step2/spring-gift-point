package gift.service;

import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void addPoints(Long memberId, int points) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.addPoints(points);
        memberRepository.save(member);
    }

    @Transactional
    public void addPointsByEmail(String email, int points) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("Member not found");
        }
        member.addPoints(points);
        memberRepository.save(member);
    }

    @Transactional
    public void usePoints(Long memberId, int points) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.subtractPoints(points);
        memberRepository.save(member);
    }
}