package gift.repository.point;

import gift.entity.point.PointChargeEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointChargeRepository extends JpaRepository<PointChargeEntity, Long> {

    Page<PointChargeEntity> findAllByUserIdAndIsRevoke(Long userId, Integer isRevoke, Pageable page);

    Optional<PointChargeEntity> findByIdAndUserIdAndIsRevoke(Long id, Long userId, Integer isRevoke);
}
