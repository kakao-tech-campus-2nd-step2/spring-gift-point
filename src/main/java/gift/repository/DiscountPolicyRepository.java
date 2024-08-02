package gift.repository;

import gift.entity.DiscountPolicyEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicyEntity,Long> {
    Page<DiscountPolicyEntity> findAllByIsDeleteAndEndDateLessThanEqual(Integer isDelete, Pageable page, LocalDateTime now);
    Optional<DiscountPolicyEntity> findByIdAndIsDeleteAndEndDateLessThanEqual(Long id, Integer isDelete, LocalDateTime now);
}
