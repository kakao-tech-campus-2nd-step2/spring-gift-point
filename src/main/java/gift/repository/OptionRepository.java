package gift.repository;

import gift.model.Option;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Option o where o.id = :optionId and o.product.id = :productId")
    Optional<Option> findByIdAndProductIdWithLock(
        @Param("productId") Long productId,
        @Param("optionId") Long optionId
    );
}
