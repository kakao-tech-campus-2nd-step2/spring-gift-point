package gift.service;

import gift.dto.point.PointResponse;
import gift.exception.NotFoundElementException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PointService {

    private final MemberRepository memberRepository;

    public PointService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PointResponse addPoint(Long memberId, Integer point) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        member.addPoint(point);
        memberRepository.save(member);
        return PointResponse.of(member.getPoint());
    }

    public void subtractPoint(Long memberId, Integer point) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        member.subtractPoint(point);
        memberRepository.save(member);
    }

    public PointResponse getPoint(Long memberId) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        return PointResponse.of(member.getPoint());
    }
}
