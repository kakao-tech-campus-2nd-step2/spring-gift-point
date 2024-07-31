package gift.product;

import gift.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  
    boolean existsByCategoryId(Long categoryId);
}
