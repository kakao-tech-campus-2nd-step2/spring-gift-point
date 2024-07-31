package gift.doamin.category.service;

import gift.doamin.category.dto.CategoryForm;
import gift.doamin.category.dto.CategoryParam;
import gift.doamin.category.entity.Category;
import gift.doamin.category.exception.CategoryNotFoundException;
import gift.doamin.category.repository.JpaCategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final JpaCategoryRepository categoryRepository;

    public CategoryService(JpaCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(CategoryForm categoryForm) {
        categoryRepository.save(new Category(categoryForm.getName()));
    }

    public List<CategoryParam> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryParam::new).toList();
    }

    public CategoryParam getCategory(Long id) {
        return categoryRepository.findById(id).map(CategoryParam::new)
            .orElseThrow(CategoryNotFoundException::new);
    }

    public void updateCategory(CategoryForm categoryForm) {

        Category category = categoryRepository.findById(categoryForm.getId())
            .orElseThrow(CategoryNotFoundException::new);

        category.update(categoryForm);

        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
