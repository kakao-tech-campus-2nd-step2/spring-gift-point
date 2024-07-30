package gift.repository;

import gift.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void delete(Product product);
    void deleteById(Long id);
}
