package gift.service;

import gift.dto.PointDTO;
import gift.model.entity.Member;
import gift.model.entity.Point;
import gift.model.entity.Product;
import gift.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public void addPoint(PointDTO pointDTO) {
        Optional<Point> OptionalPoint = pointRepository.findByMember_Id(pointDTO.getMemberId());
        OptionalPoint.ifPresent(point -> {
            pointRepository.save(point.updatePoint(pointDTO.getPoints()));
        });
    }

    public void usePoint(Member member, Product product) {
        Optional<Point> OptionalPoint = pointRepository.findByMember_Id(member.getId());
        Long updatePoints = (long) (product.getPrice() * 0.1);
        OptionalPoint.ifPresent(point -> {
            pointRepository.save(point.updatePoint(-updatePoints));
        });
    }
}
