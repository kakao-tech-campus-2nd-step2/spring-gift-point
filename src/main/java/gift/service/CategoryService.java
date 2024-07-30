package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Category;
import gift.model.CategoryDTO;
import gift.model.CategoryPageDTO;
import gift.model.Product;
import gift.model.ProductDTO;
import gift.repository.CategoryRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.id(), categoryDTO.name(), categoryDTO.color(),
            categoryDTO.imageUrl(), categoryDTO.description());
        return convertToDTO(categoryRepository.save(category));
    }

    public CategoryPageDTO findCategoryPage(Pageable pageable) {
        List<CategoryDTO> categories = categoryRepository.findAll(pageable)
            .map(this::convertToDTO)
            .stream()
            .toList();
        return new CategoryPageDTO(pageable.getPageNumber(), pageable.getPageSize(),
            categories.size(), categories);
    }

    public CategoryDTO findCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.CATEGORY_NOT_FOUND, categoryId));
        return convertToDTO(category);
    }

    public List<ProductDTO> findProductsInCategory(long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.CATEGORY_NOT_FOUND, categoryId));
        List<ProductDTO> products =  category.getProducts()
            .stream()
            .map(this::productConvertToDTO)
            .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());
        return products.subList(start, end);
    }

    public CategoryDTO updateCategory(long categoryId, CategoryDTO categoryDTO) {
        Category updateCategory = new Category(categoryId, categoryDTO.name(),
            categoryDTO.color(), categoryDTO.imageUrl(), categoryDTO.description());
        return convertToDTO(categoryRepository.save(updateCategory));
    }

    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }

    private ProductDTO productConvertToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }
}
