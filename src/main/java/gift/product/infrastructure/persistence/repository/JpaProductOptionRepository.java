package gift.product.infrastructure.persistence.repository;

import gift.product.infrastructure.persistence.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {

    List<ProductOptionEntity> findAllByProductId(Long productId);

    Long countByProductId(Long productId);

    boolean existsByProductIdAndName(Long productId, String name);

}
