package gift.category;

import gift.category.model.Category;
import gift.category.model.CategoryRequest;
import gift.category.model.CategoryResponse;
import gift.common.exception.CategoryException;
import gift.product.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CategoryService(ProductRepository productRepository,
        CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
            .map(CategoryResponse::from)
            .getContent();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) throws CategoryException {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryException(CategoryErrorCode.NOT_FOUND));
        return CategoryResponse.from(category);
    }

    @Transactional
    public Long insertCategory(CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.name(), categoryRequest.color(),
            categoryRequest.imageUrl(), categoryRequest.description());
        category = categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void updateCategory(CategoryRequest categoryRequest, Long id) throws CategoryException {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryException(CategoryErrorCode.NOT_FOUND));
        category.updateInfo(categoryRequest.name(), categoryRequest.color(),
            categoryRequest.imageUrl(), categoryRequest.description());
    }

    @Transactional
    public void deleteCategory(Long id) throws CategoryException {
        if (productRepository.existsByCategoryId(id)) {
            throw new CategoryException(CategoryErrorCode.CAN_NOT_DELETE);
        }
        categoryRepository.deleteById(id);
    }
}
