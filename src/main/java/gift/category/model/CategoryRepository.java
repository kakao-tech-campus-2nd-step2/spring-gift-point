package gift.category.model;

import gift.category.model.dto.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrue();

    Optional<Category> findByIdAndIsActiveTrue(Long id);
}
