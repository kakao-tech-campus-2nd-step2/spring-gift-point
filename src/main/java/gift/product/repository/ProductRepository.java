package gift.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import gift.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


}

