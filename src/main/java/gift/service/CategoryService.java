package gift.service;

import gift.common.dto.PageResponse;
import gift.common.exception.CategoryException;
import gift.common.exception.ErrorCode;
import gift.controller.category.dto.CategoryRequest;
import gift.controller.category.dto.CategoryResponse;
import gift.model.Category;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long register(CategoryRequest.Create request) {
        Category category = categoryRepository.save(request.toEntity());
        return category.getId();
    }

    public CategoryResponse.InfoList findAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return CategoryResponse.InfoList.from(categoryList);
    }

    public CategoryResponse.Info findCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryResponse.Info.from(category);
    }

    @Transactional
    public CategoryResponse.Info updateCategory(Long id, CategoryRequest.Update request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryException(ErrorCode.CATEGORY_NOT_FOUND));
        category.updateCategory(request.name(), request.color(), request.imageUrl(),
            request.description());
        return CategoryResponse.Info.from(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CategoryException(ErrorCode.CATEGORY_CANNOT_DELETE);
        }

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        categoryRepository.deleteById(categoryId);
    }
}
