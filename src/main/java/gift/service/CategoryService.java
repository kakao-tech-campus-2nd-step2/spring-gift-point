package gift.service;

import gift.converter.CategoryConverter;
import gift.dto.CategoryDTO;
import gift.dto.PageRequestDTO;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryDTO> findAllCategories(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.toPageRequest();
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryConverter::convertToDTO);
    }

    public List<CategoryDTO> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(CategoryConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    public Long addCategory(CategoryDTO categoryDTO) {
        Category category = CategoryConverter.convertToEntity(categoryDTO);
        categoryRepository.save(category);
        return category.getId();
    }

    public Optional<CategoryDTO> findCategoryById(Long id) {
        return categoryRepository.findById(id)
            .map(CategoryConverter::convertToDTO);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingCategory.update(
            categoryDTO.getName(),
            categoryDTO.getColor(),
            categoryDTO.getImageUrl(),
            categoryDTO.getDescription()
        );
        categoryRepository.save(existingCategory);
        return CategoryConverter.convertToDTO(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}