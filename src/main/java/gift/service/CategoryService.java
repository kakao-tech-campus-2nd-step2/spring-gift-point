package gift.service;

import gift.dto.CategoryRequest;
import gift.entity.Category;
import gift.exception.CategoryHasProductsException;
import gift.exception.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(CategoryRequest request) {
        Category category = new Category(request.name(), request.color(),
            request.imageUrl(), request.description());
        return categoryRepository.save(category);
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("id에 해당하는 카테고리가 없습니다."));
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("id에 해당하는 카테고리가 없습니다."));
        category.updateCategory(request.name(), request.color(),
            request.imageUrl(), request.description());
        return category;
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("id에 해당하는 카테고리가 없습니다."));
        if (!category.emptyCategoryCheck()) {
            throw new CategoryHasProductsException("해당 카테고리에 속한 상품이 있습니다.");
        }
        categoryRepository.deleteById(id);
    }
}
