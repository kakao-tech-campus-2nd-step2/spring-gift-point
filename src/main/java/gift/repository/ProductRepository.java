package gift.repository;

import gift.model.product.Product;
import gift.model.product.ProductName;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Validated
public interface ProductRepository extends JpaRepository<@Valid Product, Long> {
    boolean existsByName(ProductName name);
    Page<Product> findAll(Pageable pageable);
}
