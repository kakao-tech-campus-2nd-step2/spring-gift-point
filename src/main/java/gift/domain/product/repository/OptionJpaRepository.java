package gift.domain.product.repository;

import gift.domain.product.entity.Option;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionJpaRepository extends JpaRepository<Option, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select o from Option o where o.id = :optionId")
    Optional<Option> findByIdWithOptimisticLock(@Param("optionId") Long optionId);

    @Query("delete from Option o where o.product.id = :productId")
    @Modifying
    void deleteAllByProductId(@Param("productId") Long productId);
}
