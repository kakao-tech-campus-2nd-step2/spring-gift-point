package gift.domain.service;

import gift.domain.dto.request.CategoryRequest;
import gift.domain.dto.response.CategoryResponse;
import gift.domain.entity.Category;
import gift.domain.exception.conflict.CategoryAlreadyExistsException;
import gift.domain.exception.notFound.CategoryNotFoundException;
import gift.domain.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::of)
            .toList();
    }

    @Transactional
    public CategoryResponse addCategory(CategoryRequest request) {
        categoryRepository.findByName(request.name()).ifPresent(c -> {
            throw new CategoryAlreadyExistsException();
        });

        return CategoryResponse.of(categoryRepository.save(request.toEntity()));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findById(id);
        // 바꾸려는 이름이 기존과 다른 경우
        // 그 이름이 다른 카테고리와 겹치는 경우 에러
        if (!category.getName().equals(request.name())) {
            categoryRepository.findByName(request.name()).ifPresent(c -> {
                throw new CategoryAlreadyExistsException();
            });
        }
        category.set(request);
        return CategoryResponse.of(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }
}
