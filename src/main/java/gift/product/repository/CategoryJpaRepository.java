package gift.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.product.domain.Category;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByName(String name);
}
