package gift.category.service;

import gift.category.dto.CategoryRequest;
import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.exception.IllegalCategoryException;
import gift.exception.IllegalProductException;
import gift.exception.ResourceNotFoundException;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryRequest> getAllCategories() {
        var allCategories = categoryRepository.findAll();

        return allCategories.stream()
            .map(category -> {
                var categoryProductNameList = category.getProductList().stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());
                return new CategoryRequest(category.getId(), category.getName(),
                    categoryProductNameList);
            })
            .collect(Collectors.toList());
    }

    public Category addCategory(CategoryRequest categoryRequest) {
        List<Product> productList = new ArrayList<>();
        if (!categoryRequest.getProductName().isEmpty()) {
            productList = categoryRequest.getProductName().stream()
                .map(productRepository::findByName)
                .map(
                    product -> product.orElseThrow(() -> new IllegalArgumentException("없는 상품입니다.")))
                .toList();
        }
        return categoryRepository.save(
            new Category(categoryRequest.getName(), productList, categoryRequest.getColor(),
                categoryRequest.getImageUrl(), categoryRequest.getDescription()));
    }

    public Category getCategoryById(Long id) {
        var foundCategoryById = categoryRepository.findById(id);
        if (foundCategoryById.isEmpty()) {
            throw new ResourceNotFoundException("없는 카테고리 입니다.");
        }
        return foundCategoryById.get();
    }

    @Transactional
    public Category updateCategory(Long categoryId,CategoryRequest categoryRequest) {
        var foundCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalCategoryException("없는 카테고리 입니다."));
        foundCategory.setName(categoryRequest.getName());
        foundCategory.setColor(categoryRequest.getColor());
        foundCategory.setImageUrl(categoryRequest.getImageUrl());
        foundCategory.setDescription(categoryRequest.getDescription());
        foundCategory.getProductList().clear();
        for (String productName : categoryRequest.getProductName()) {
            foundCategory.getProductList().add(productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalProductException("없는 상품입니다.")));
        }
        return categoryRepository.save(foundCategory);
    }


    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalCategoryException("없는 카테고리 입니다."));
        categoryRepository.deleteById(category.getId());
    }
}
