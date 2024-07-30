package gift.service;

import gift.dto.CategoryResponseDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private ProductService productService;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryResponseDto> getAllCategoryResponseDto() {
        return getAllCategories().stream().map(this::fromEntity).toList();
    }

    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByName(categoryName);
    }

    public CategoryResponseDto getCategoryDtoByProductId(Long productId) {
        Product product = productService.findById(productId);
        Long categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).get();
        return fromEntity(category);
    }

    public CategoryResponseDto fromEntity(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getColor(), category.getDescription(), category.getImageUrl());
    }
}
