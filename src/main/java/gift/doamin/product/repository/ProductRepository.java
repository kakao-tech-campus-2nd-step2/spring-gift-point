package gift.doamin.product.repository;

import gift.doamin.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long productId);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);

    void deleteById(Long id);
}
