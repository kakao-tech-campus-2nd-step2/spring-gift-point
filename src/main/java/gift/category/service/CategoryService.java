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
        return categoryRepository.findAll().stream()
            .map(this::convertEntityToDTO)
            .toList();
    }

    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id)
            .map(this::convertEntityToDTO);
    }

    public CategoryDTO save(CategoryDTO categoryDTO) {
        categoryRepository.save(convertDTOToEntity(categoryDTO));
        return categoryDTO;
    }

    public CategoryDTO update(Long categoryId, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.id())
            .orElseThrow(() -> new EntityNotFoundException("Category Id " + categoryDTO.id() + "가 없습니다."));

        category.setName(categoryDTO.name());
        category.setDescription(categoryDTO.description());
        category.setColor(categoryDTO.color());
        category.setImageUrl(categoryDTO.imageUrl());

        return convertEntityToDTO(categoryRepository.save(category));
    }


    public CategoryDTO convertEntityToDTO(Category category){
        return new CategoryDTO(category.getId(),
            category.getName(),
            category.getDescription(),
            category.getColor(),
            category.getImageUrl());
    }
    public Category convertDTOToEntity(CategoryDTO categoryDTO){
        return new Category(categoryDTO.id(),
            categoryDTO.name(),
            categoryDTO.description(),
            categoryDTO.color(),
            categoryDTO.imageUrl());
    }
}
