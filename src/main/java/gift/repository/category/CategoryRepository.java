package gift.repository.category;

import gift.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
