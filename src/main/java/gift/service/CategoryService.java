package gift.service;

import gift.domain.Category;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        existingCategory.setName(category.getName());
        existingCategory.setColor(category.getColor());
        existingCategory.setImageUrl(category.getImageUrl());
        existingCategory.setDescription(category.getDescription());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        categoryRepository.delete(category);
    }

}
