package gift.repository;

import gift.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    Boolean existsByName(String name);

    Optional<Category> findById(Long id);

    void deleteById(Long id);
}
