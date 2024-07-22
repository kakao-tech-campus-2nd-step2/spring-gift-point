package gift.service;

import static gift.util.constants.CategoryConstants.CATEGORY_NOT_FOUND;

import gift.dto.category.CategoryCreateRequest;
import gift.dto.category.CategoryResponse;
import gift.dto.category.CategoryUpdateRequest;
import gift.exception.category.CategoryNotFoundException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND + id));
    }

    public CategoryResponse addCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = convertToEntity(categoryCreateRequest);
        Category addedCategory = categoryRepository.save(category);
        return convertToDTO(addedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND + id));

        category.update(
            categoryUpdateRequest.name(),
            categoryUpdateRequest.color(),
            categoryUpdateRequest.imageUrl(),
            categoryUpdateRequest.description()
        );
        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    // Mapper methods
    private CategoryResponse convertToDTO(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    private Category convertToEntity(CategoryCreateRequest categoryCreateRequest) {
        return new Category(
            categoryCreateRequest.name(),
            categoryCreateRequest.color(),
            categoryCreateRequest.imageUrl(),
            categoryCreateRequest.description()
        );
    }
}
