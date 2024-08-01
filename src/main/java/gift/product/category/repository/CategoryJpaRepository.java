package gift.product.category.repository;

import gift.product.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}
