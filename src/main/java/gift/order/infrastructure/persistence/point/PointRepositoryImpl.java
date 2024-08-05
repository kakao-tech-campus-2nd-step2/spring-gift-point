package gift.order.infrastructure.persistence.point;

import gift.core.domain.order.PointRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository {
    private final JpaPointRepository jpaPointRepository;

    public PointRepositoryImpl(JpaPointRepository jpaPointRepository) {
        this.jpaPointRepository = jpaPointRepository;
    }

    @Override
    public Long getPoint(Long userId) {
        return jpaPointRepository.findById(userId).orElseGet(
                () -> jpaPointRepository.save(new PointEntity(userId, 0L))
        ).getPoint();
    }
}
