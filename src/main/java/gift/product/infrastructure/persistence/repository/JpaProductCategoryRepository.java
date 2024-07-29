package gift.product.infrastructure.persistence.repository;

import gift.product.infrastructure.persistence.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    Optional<ProductCategoryEntity> findByName(String name);

}
