package gift.service;

import gift.dto.PointRequest;
import gift.dto.PointResponse;
import gift.model.Member;
import gift.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PointService {
    private final MemberRepository memberRepository;

    public PointService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void pointCharge(PointRequest request, Member member) {
        member.chargePoint(request.point());
    }

    public PointResponse getPoint(Member member) {
        return new PointResponse(member.getPoint());
    }

    public void subtractPoint(int point, Member member) {
        member.subtractPoint(point);
    }
}
