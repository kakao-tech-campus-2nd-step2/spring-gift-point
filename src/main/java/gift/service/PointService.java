package gift.service;

import gift.dto.point.PointRequest;
import gift.dto.point.PointResponse;
import gift.entity.Member;
import gift.entity.Point;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PointService(PointRepository pointRepository, MemberRepository memberRepository) {
        this.pointRepository = pointRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public PointResponse getPoints(Long memberId) {
        Point point = pointRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException.EntityNotFoundException("Points not found"));
        return mapToPointResponse(point);
    }

    @Transactional
    public PointResponse addPoint(PointRequest pointRequest) {
        Member member = memberRepository.findById(pointRequest.getMemberId()).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));
        Point point = pointRepository.findByMemberId(member.getId()).orElse(new Point(0, member));
        point.addPoints(pointRequest.getPoints());
        Point savedPoint = pointRepository.save(point);

        return mapToPointResponse(savedPoint);
    }

    private PointResponse mapToPointResponse(Point point) {
        PointResponse pointResponse = new PointResponse();
        pointResponse.setMemberId(point.getMember().getId());
        pointResponse.setBalance(point.getBalance());
        return pointResponse;
    }

    @Transactional
    public PointResponse usePoints(PointRequest pointRequest) {
        Point point = pointRepository.findByMemberId(pointRequest.getMemberId())
                .orElseThrow(() -> new CustomException.EntityNotFoundException("Points not found for member"));

        point.deductPoints(pointRequest.getPoints()); // 포인트 차감
        pointRepository.save(point);

        return new PointResponse(point.getMember().getId(), point.getBalance());
    }
}
