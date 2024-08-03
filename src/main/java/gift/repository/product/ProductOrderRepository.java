package gift.repository.product;

import gift.entity.product.ProductOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity,Long> {

    Page<ProductOrderEntity> findAllByUserEntityId(long userId, Pageable pageable);
}
