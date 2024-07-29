package gift.category.service;

import gift.category.domain.Category;
import gift.category.domain.CategoryDTO;
import gift.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(category -> new CategoryDTO(category.getId(), category.getName()))
            .toList();
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id)
            .map(category -> new CategoryDTO(category.getId(), category.getName()));
    }

    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        categoryRepository.save(category);
        return categoryDTO;
    }

    public CategoryDTO update(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.id())
            .orElseThrow(() -> new EntityNotFoundException("Category Id " + categoryDTO.id() + "가 없습니다."));
        category.setName(categoryDTO.name());
        categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }
}
