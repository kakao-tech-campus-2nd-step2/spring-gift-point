package gift.domain.service;

import gift.domain.dto.request.CategoryRequest;
import gift.domain.dto.response.CategoryResponse;
import gift.domain.entity.Category;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
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
        return categoryRepository.findById(id).orElseThrow(() -> new ServerException(ErrorCode.CATEGORY_NOT_FOUND));
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
            throw new ServerException(ErrorCode.CATEGORY_ALREADY_EXISTS);
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
                throw new ServerException(ErrorCode.CATEGORY_ALREADY_EXISTS);
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
