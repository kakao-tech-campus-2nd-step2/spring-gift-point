package gift.category;

import gift.exception.AlreadyExistCategory;
import gift.exception.InvalidCategory;
import gift.product.Product;
import gift.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponseDto> getAllCategory() {
        return categoryRepository.findAll().stream()
            .map(category -> new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()))
            .collect(Collectors.toList());
    }

    public CategoryResponseDto postCategory(CategoryRequestDto categoryRequestDto) {
        Category category = new Category(
            categoryRequestDto.name(),
            categoryRequestDto.color(),
            categoryRequestDto.imageUrl(),
            categoryRequestDto.description()
        );
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new AlreadyExistCategory("동일한 카테고리가 이미 존재합니다.");
        }

        categoryRepository.saveAndFlush(category);

        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    public CategoryResponseDto putCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new InvalidCategory("유효하지 않은 카테고리입니다."));

        category.update(categoryRequestDto.name(), categoryRequestDto.color(), categoryRequestDto.imageUrl(), categoryRequestDto.description());
        categoryRepository.saveAndFlush(category);

        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new InvalidCategory("유효하지 않은 카테고리입니다."));
        categoryRepository.delete(category);
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new InvalidCategory("유효하지 않은 카테고리입니다."));

        return new CategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

}
