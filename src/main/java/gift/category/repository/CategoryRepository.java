package gift.category.repository;

import gift.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  boolean existsByName(String name);

  boolean existsById(Long id);
}
