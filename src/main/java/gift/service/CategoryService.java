package gift.service;

import gift.dto.request.AddCategoryRequest;
import gift.dto.request.UpdateCategoryRequest;
import gift.dto.response.CategoryIdResponse;
import gift.dto.response.CategoryResponse;
import gift.entity.Category;
import gift.exception.CategoryNameDuplicateException;
import gift.exception.CategoryNotFoundException;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategoryResponses() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromCategory)
                .toList();
    }

    public CategoryIdResponse addCategory(AddCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new CategoryNameDuplicateException(request.name());
        }

        Category newCategory = new Category(
                request.name(),
                request.color(),
                request.imageUrl(),
                request.description()
        );

        return new CategoryIdResponse(categoryRepository.save(newCategory).getId());
    }

    @Transactional
    public void updateCategory(UpdateCategoryRequest request) {
        Category updateTargetCategory = categoryRepository.findById(request.id())
                .orElseThrow(() -> new CategoryNotFoundException(request.id()));
        updateTargetCategory.update(request.name(), request.color(), request.imageUrl(), request.description());
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    public void deleteCategory(Long categoryId) {
        Category deleteTargetCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.delete(deleteTargetCategory);
    }
}
