package gift.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse insertNewCategory(CategoryRequest newCategory) {
        Category category = new Category(newCategory.name(), newCategory.color(), newCategory.imageUrl(), newCategory.description());
        return new CategoryResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponse::new).toList();
    }

    public CategoryResponse findCategoriesByID(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        return new CategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategoriesByID(Long id, CategoryRequest updateParam) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.updateWithRequest(updateParam);
        return new CategoryResponse(category);
    }

    public void deleteCategoriesByID(Long id) {
        categoryRepository.deleteById(id);
    }
}
