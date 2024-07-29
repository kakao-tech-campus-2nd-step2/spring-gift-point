package gift.repository.product;

import gift.model.product.Category;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    Category save(Category category);

    void deleteById(Long id);

    List<Category> findAll();

    Optional<Category> findByName(String name);
}
