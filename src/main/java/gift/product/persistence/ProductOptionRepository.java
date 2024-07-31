package gift.product.persistence;

import gift.product.domain.ProductOption;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Optional<ProductOption> findByProductIdAndId(Long productId, Long id);

    List<ProductOption> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select po from ProductOption po where po.product.id = :productId and po.id = :id")
    Optional<ProductOption> findByProductIdAndIdForUpdate(Long productId, Long id);
}