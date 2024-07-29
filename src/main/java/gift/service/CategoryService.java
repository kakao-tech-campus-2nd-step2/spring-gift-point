package gift.service;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import gift.exception.DuplicatedNameException;
import gift.exception.NotFoundElementException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public CategoryService(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        categoryNameValidation(categoryRequest.name());
        var category = saveCategoryWithCategoryRequest(categoryRequest);
        return getCategoryResponseFromCategory(category);
    }

    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        categoryNameValidation(categoryRequest.name());
        var category = findCategoryById(id);
        category.updateCategory(categoryRequest.name(), categoryRequest.description(), categoryRequest.color(), categoryRequest.imageUrl());
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        var category = findCategoryById(id);
        return getCategoryResponseFromCategory(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(this::getCategoryResponseFromCategory)
                .toList();
    }

    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundElementException("존재하지 않는 상품 카테고리의 ID 입니다.");
        }
        productService.deleteAllProductWithCategoryId(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private Category saveCategoryWithCategoryRequest(CategoryRequest categoryRequest) {
        var category = new Category(categoryRequest.name(), categoryRequest.description(), categoryRequest.color(), categoryRequest.imageUrl());
        return categoryRepository.save(category);
    }

    private CategoryResponse getCategoryResponseFromCategory(Category category) {
        return CategoryResponse.of(category.getId(), category.getName(), category.getDescription(), category.getColor(), category.getImageUrl());
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품 카테고리가 존재하지 않습니다."));
    }

    private void categoryNameValidation(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicatedNameException("이미 존재하는 카테고리 이름입니다.");
        }
    }
}
