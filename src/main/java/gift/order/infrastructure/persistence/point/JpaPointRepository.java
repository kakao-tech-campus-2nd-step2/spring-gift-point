package gift.order.infrastructure.persistence.point;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointRepository extends JpaRepository<PointEntity, Long> {
}
