package gift.repository;

import gift.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaDao extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

}
