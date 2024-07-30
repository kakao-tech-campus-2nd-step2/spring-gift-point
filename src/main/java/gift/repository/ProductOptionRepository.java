package gift.repository;

import gift.entity.ProductOptionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {

    Page<ProductOptionEntity> findAllByProductId(long productId, Pageable pageable);

    List<ProductOptionEntity> findAllByProductId(long productId);

    Optional<ProductOptionEntity> findByProductIdAndId(long productId, long id);

    Optional<ProductOptionEntity> findByNameAndProductId(String name, Long id);
}
