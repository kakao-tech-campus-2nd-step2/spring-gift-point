package gift.doamin.category.repository;

import gift.doamin.category.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    boolean existsByName(String name);

    void deleteById(Long id);
}
