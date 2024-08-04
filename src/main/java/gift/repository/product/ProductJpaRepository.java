package gift.repository.product;

import gift.model.product.Product;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {

    @Override
    @Query("SELECT p FROM Product p ORDER BY p.price")
    Page<Product> findAllOrderByPrice(Pageable pageable);

    @Override
    Page<Product> findByNameContaining(String name, Pageable pageable);

    @Override
    Optional<Product> findById(Long id);

    @Override
    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.options")
    Page<Product> findAll(Pageable pageable);


}
