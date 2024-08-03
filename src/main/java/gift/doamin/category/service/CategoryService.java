package gift.doamin.category.service;

import gift.doamin.category.dto.CategoryRequest;
import gift.doamin.category.dto.CategoryResponse;
import gift.doamin.category.entity.Category;
import gift.doamin.category.exception.CategoryNotFoundException;
import gift.doamin.category.repository.JpaCategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final JpaCategoryRepository categoryRepository;

    public CategoryService(JpaCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new IllegalArgumentException("중복된 카테고리명입니다.");
        }

        Category category = categoryRepository.save(categoryRequest.toEntity());
        return new CategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponse::new).toList();
    }

    public CategoryResponse getCategory(Long id) {
        return categoryRepository.findById(id).map(CategoryResponse::new)
            .orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest) {

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotFoundException::new);

        category.update(categoryRequest);

        return new CategoryResponse(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
