package gift.Repository;

import gift.Entity.OptionEntity;
import gift.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {
    List<OptionEntity> findByProductId(Long productId);
    List<OptionEntity> findByProduct(ProductEntity product);
}
