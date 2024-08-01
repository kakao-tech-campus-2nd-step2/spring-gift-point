package gift.domain.category.service;

import gift.domain.category.dto.CategoryRequest;
import gift.domain.category.dto.CategoryResponse;
import gift.domain.category.entity.Category;
import gift.domain.category.exception.CategoryNotFoundException;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.option.repository.OptionRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductRepository productRepository,
        OptionRepository optionRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll().stream().map(this::entityToDto).toList();
    }

    @Transactional
    public void createCategory(CategoryRequest request) {
        categoryRepository.save(dtoToEntity(request));
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        Category savedCategory = categoryRepository.findById(id).orElseThrow();
        savedCategory.updateAll(request.getName(), request.getColor(), request.getImageUrl(),
            request.getDescription());
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category savedCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("해당 카테고리가 존재하지 않습니다."));
        List<Product> productList = productRepository.findAllByCategory(savedCategory);
        productList.forEach(optionRepository::deleteByProduct);
        productRepository.deleteByCategory(savedCategory);
        categoryRepository.delete(savedCategory);
    }

    private CategoryResponse entityToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }

    private Category dtoToEntity(CategoryRequest request) {
        return new Category(request.getName(), request.getColor(), request.getImageUrl(),
            request.getDescription());
    }

}
