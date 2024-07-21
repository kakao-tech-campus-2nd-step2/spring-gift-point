package gift.category.service;

import gift.category.model.CategoryRepository;
import gift.category.model.dto.Category;
import gift.category.model.dto.CategoryRequest;
import gift.category.model.dto.CategoryResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public CategoryResponse findCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.map(CategoryResponse::new)
                .orElseThrow(() -> new EntityNotFoundException("Category"));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addCategory(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.name(), categoryRequest.description());
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new EntityNotFoundException("Category"));
        category.updateCategory(categoryRequest.name(), categoryRequest.description());
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new EntityNotFoundException("Category"));
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional.orElseThrow(() -> new EntityNotFoundException("Category"));
    }
}
