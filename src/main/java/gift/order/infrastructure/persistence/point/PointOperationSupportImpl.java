package gift.order.infrastructure.persistence.point;

import gift.order.service.PointOperationSupport;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PointOperationSupportImpl implements PointOperationSupport {
    private final JpaPointRepository jpaPointRepository;

    public PointOperationSupportImpl(JpaPointRepository jpaPointRepository) {
        this.jpaPointRepository = jpaPointRepository;
    }

    @Override
    @Transactional
    public void addPoint(Long userId, Long point) {
        PointEntity pointEntity = jpaPointRepository
                .findById(userId)
                .orElseGet(() -> PointEntity.from(userId, 0L));
        pointEntity.addPoint(point);
        jpaPointRepository.save(pointEntity);
    }

    @Override
    @Transactional
    public void subtractPoint(Long userId, Long point) {
        PointEntity pointEntity = jpaPointRepository
                .findById(userId)
                .orElseGet(() -> PointEntity.from(userId, 0L));
        pointEntity.subtractPoint(point);
        jpaPointRepository.save(pointEntity);
    }
}
