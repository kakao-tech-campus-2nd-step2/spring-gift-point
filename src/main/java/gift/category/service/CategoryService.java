package gift.category.service;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.product.model.Product;
import gift.product.model.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 1. 생성 로직
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    // 2. 수정 로직
    public void updateCategory(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
        existingCategory.setId(id);
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
    }

    // 3. 삭제 로직
    public void deleteCategory(Long id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found"));
        categoryRepository.delete(existingCategory);
    }

    // 4. 조회 로직
    // 상품 전부 조회
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<Category> categories = categoryRepository.findAll();
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                productDTOs.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()));
            }
        }

        return productDTOs;
    }

    // 카테고리 전부 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
