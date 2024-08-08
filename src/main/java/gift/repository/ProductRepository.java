package gift.repository;

import gift.domain.product.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIdIn(List<Long> productIds);

    Page<Product> findByIdIn(List<Long> productIds, Pageable pageable);
}
