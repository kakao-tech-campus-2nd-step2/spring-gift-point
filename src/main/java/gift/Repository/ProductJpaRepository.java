package gift.Repository;

import gift.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findByisDeletedFalse();

    Page<Product> findByisDeletedFalse(Pageable pageable);
}
