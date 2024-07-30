package gift.service;

import gift.domain.Category;
import gift.domain.CategoryName;
import gift.dto.CategoryDTO;
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

    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryDTO::from)
            .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return CategoryDTO.from(category);
    }

    public CategoryDTO getCategoryByName(CategoryName name) {
        Category category = categoryRepository.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return CategoryDTO.from(category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryDTO.toEntity();
        Category savedCategory = categoryRepository.save(category);
        return CategoryDTO.from(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Category updatedCategory = new Category.CategoryBuilder()
            .id(category.getId())
            .name(categoryDTO.getName())
            .color(categoryDTO.getColor())
            .imageUrl(categoryDTO.getImageUrl())
            .description(categoryDTO.getDescription())
            .build();

        categoryRepository.save(updatedCategory);
        return CategoryDTO.from(updatedCategory);
    }
}
