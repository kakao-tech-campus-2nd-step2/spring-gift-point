package gift.product.persistence.repository;

import gift.product.persistence.entity.Category;
import java.util.List;

public interface CategoryRepository {

    Category getReferencedCategory(Long categoryId);

    List<Category> getAllCategories();

    Long saveCategory(Category category);

    Category getCategory(Long id);

    Long deleteCategory(Long id);
}
