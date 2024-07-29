package gift.product.repository;

import gift.product.model.Option;
import gift.product.model.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    @Transactional
    @Modifying
    void deleteByProductId(Long productId);
    Page<Option> findAllByProductId(Long productId, Pageable pageable);
    List<Option> findAllByProduct(Product product);
    @Query("select count(*) from Option op where op.product.id = ?1")
    int countByProduct(Long productId);
    boolean existsByProductId(Long productId);
}
