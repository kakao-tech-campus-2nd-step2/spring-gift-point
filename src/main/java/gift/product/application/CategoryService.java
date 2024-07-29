package gift.product.application;

import gift.product.dao.CategoryRepository;
import gift.product.dto.CategoryRequest;
import gift.product.dto.CategoryResponse;
import gift.product.util.CategoryMapper;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponseDto)
                .toList();
    }

    public CategoryResponse getCategoryByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toResponseDto)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        return CategoryMapper.toResponseDto(
                categoryRepository.save(CategoryMapper.toEntity(request))
        );
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
                .update(request);
    }

}
