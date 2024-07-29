package gift.service;


import gift.exception.ProductNotFoundException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void updateCategory(Long id, Category category) {
        if (!categoryRepository.existsById(id)) {
            throw new ProductNotFoundException("Category not found");
        }
        category.setId(id);
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ProductNotFoundException("Category not found");
        }
        Category category = new Category(categoryRepository.findById(id).get().getCategoryName());
        category.setId(id);
        return category;
    }


    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ProductNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    public boolean existsCategory(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }
}
