package gift.repository;

import gift.entity.PointPaymentEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointPaymentRepository extends JpaRepository<PointPaymentEntity, Long> {
    Page<PointPaymentEntity> findAllByUserIdAndIsRevoke(Long userId, Integer isRevoke, Pageable pageable);

    Optional<PointPaymentEntity> findByIdAndUserIdAndIsRevoke(Long id, Long userId, Integer isRevoke);
}
