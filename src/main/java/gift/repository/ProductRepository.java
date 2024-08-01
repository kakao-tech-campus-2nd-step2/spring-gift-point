package gift.repository;

import gift.domain.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    List<Product> findByCategoryId(Long categoryId);

}
