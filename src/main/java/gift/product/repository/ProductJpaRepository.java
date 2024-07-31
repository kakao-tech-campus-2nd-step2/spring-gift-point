package gift.product.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.product.domain.Product;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
	Page<Product> findAll(Pageable pageable);
	Page<Product> findByNameContaining(String name, Pageable pageable);
	Page<Product> findByCategoryName(String categoryName, Pageable pageable);
}
