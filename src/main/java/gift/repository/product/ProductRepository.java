package gift.repository.product;

import gift.model.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);

    Product save(Product entity);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllOrderByPrice(Pageable pageable);

    Page<Product> findByNameContaining(String name, Pageable pageable);

    Page<Product> findByCategoryId(String searchValue, Pageable pageable);

}
