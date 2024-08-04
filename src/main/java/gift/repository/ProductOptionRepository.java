package gift.repository;

import gift.model.middle.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    List<ProductOption> findByProductId(Long productId);

    Optional<ProductOption> findByProductIdAndOptionId(Long productId, Long optionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductOption po WHERE po.product.id = :productId AND po.option.id = :optionId")
    void deleteByProductIdAndOptionId(Long productId, Long optionId);
}
