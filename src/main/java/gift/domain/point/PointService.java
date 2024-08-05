package gift.domain.point;

import gift.domain.member.JpaMemberRepository;
import gift.domain.member.Member;
import gift.global.exception.user.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final JpaMemberRepository memberRepository;

    public PointService(JpaMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 포인트 조회
     */
    public int getPoint(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
        return member.getPoint();
    }

    /**
     * 포인트 충전
     */
    @Transactional
    public void chargePoint(Long memberId, int point) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
        member.chargePoint(point);
    }
}
