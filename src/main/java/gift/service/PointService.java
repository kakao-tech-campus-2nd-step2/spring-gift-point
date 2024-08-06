package gift.service;

import gift.domain.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final MemberRepository memberRepository;

    public PointService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void addPoint(Long memberId, int point) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        member.addPoint(point);
        memberRepository.save(member);
    }

    public int getPoint(Long memberId) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        return member.getPoint();
    }

    @Transactional
    public void subtractPoint(Long memberId, int price) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        member.usePoint(price);
        memberRepository.save(member);
    }
}
