package gift.repository;

import gift.domain.Category;
import gift.domain.CategoryName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryName name);
}
