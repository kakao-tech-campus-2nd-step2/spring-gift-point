package gift.service;

import gift.entity.Member;
import gift.entity.Point;
import gift.repository.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PointService {
    private final PointRepository pointRepository;

    @Autowired
    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional
    public void subtractPoint(Member member, int point) {
        Point totalPoint = pointRepository.findByMember(member);
        totalPoint.subtractPoint(point);
    }

    @Transactional
    public void addPoint(Member member, int point) {
        Point totalPoint = pointRepository.findByMember(member);
        totalPoint.addPoint(point);
    }
}
