package gift.service;

import gift.dto.CategoryDTO;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductRepository productRepository, WishlistRepository wishlistRepository,
        OptionRepository optionRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionRepository = optionRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public void saveCategory(CategoryDTO categoryDTO) {
        categoryRepository.save(toEntity(categoryDTO, null));
    }

    @Transactional
    public void updateCategory(CategoryDTO categoryDTO, Long id) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory != null) {
            existingCategory.updateName(categoryDTO.name());
            categoryRepository.save(existingCategory);
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            List<Product> products = productRepository.findAllByCategoryId(id);
            if (!products.isEmpty()) {
                List<Long> productIds = products.stream().map(Product::getId).toList();
                wishlistRepository.deleteByProductIn(products);
                optionRepository.deleteAllByProductIdIn(productIds);
                productRepository.deleteAll(products);
            }
            categoryRepository.delete(category);
        }
    }

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getName());
    }

    public static Category toEntity(CategoryDTO categoryDTO, Long id) {
        Category category = new Category(id, categoryDTO.name());
        return category;
    }

}
