package gift.repository.product;

import gift.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpringDataJpaRepository extends JpaRepository<Product, Long> {
}