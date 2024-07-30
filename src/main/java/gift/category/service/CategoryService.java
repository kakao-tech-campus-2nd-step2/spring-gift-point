package gift.category.service;

import gift.category.domain.Category;
import gift.category.dto.CategoryServiceDto;
import gift.category.exception.CategoryNotFoundException;
import gift.category.repository.CategoryRepository;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

    public Category createCategory(CategoryServiceDto categoryServiceDto) {
        return categoryRepository.save(categoryServiceDto.toCategory());
    }

    public Category updateCategory(CategoryServiceDto categoryServiceDto) {
        validateCategoryExists(categoryServiceDto.id());
        return categoryRepository.save(categoryServiceDto.toCategory());
    }

    @Transactional
    public void deleteCategory(Long id) {
        validateCategoryExists(id);
        setProductCategoryNull(id);
        categoryRepository.deleteById(id);
    }

    private void setProductCategoryNull(Long id) {
        Category category = getCategoryById(id);

        if (category.getProducts().isEmpty())
            return;

        productRepository.setCategoryNullByCategoryId(id);
        entityManager.flush();
        entityManager.clear();
    }

    private void validateCategoryExists(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
    }

}
