package gift.repository.point;

import gift.entity.point.DiscountPolicyEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicyEntity,Long> {
    Page<DiscountPolicyEntity> findAllByIsDeleteAndEndDateAfter(Integer isDelete, Pageable page, LocalDateTime now);
    Optional<DiscountPolicyEntity> findByIdAndIsDeleteAndEndDateAfter(Long id, Integer isDelete, LocalDateTime now);
}
