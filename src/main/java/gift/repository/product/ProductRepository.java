package gift.repository.product;

import gift.model.category.Category;
import gift.model.product.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllByCategory(Category category, Pageable pageable);
    List<Product> findAllByCategoryId(Long CategoryId);
}