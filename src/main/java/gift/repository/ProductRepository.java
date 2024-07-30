package gift.repository;

import gift.entity.ProductEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByNameContaining(String name);

    Page<ProductEntity> findAll(Pageable pageable);

    boolean existsByNameAndPriceAndImageUrl(String name, Long price, String imageUrl);

}
