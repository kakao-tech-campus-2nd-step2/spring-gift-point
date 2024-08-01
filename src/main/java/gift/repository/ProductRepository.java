package gift.repository;

import gift.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    List<Product> findByCategoryId(Long categoryId);
}
