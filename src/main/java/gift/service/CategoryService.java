package gift.service;

import gift.dto.category.CategoriesResponseDTO;
import gift.dto.category.CategoryRequestDTO;
import gift.dto.category.CategoryResponseDTO;
import gift.exceptions.CustomException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoriesResponseDTO getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return new CategoriesResponseDTO(categories);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                                 .orElseThrow(CustomException::categoryNotFoundException);
    }

    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category(categoryRequestDTO.name(),
                                         categoryRequestDTO.color(),
                                         categoryRequestDTO.imageUrl(),
                                         categoryRequestDTO.description());

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(savedCategory.getId(),
                                       savedCategory.getName(),
                                       savedCategory.getColor(),
                                       savedCategory.getImageUrl(),
                                       savedCategory.getDescription());
    }

    public CategoryResponseDTO modifyCategory(Long categoryId, CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CustomException::categoryNotFoundException);

        category.setName(categoryRequestDTO.name());
        category.setColor(categoryRequestDTO.color());
        category.setImageUrl(categoryRequestDTO.imageUrl());
        category.setDescription(categoryRequestDTO.description());

        Category updatedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(updatedCategory.getId(),
                                       updatedCategory.getName(),
                                       updatedCategory.getColor(),
                                       updatedCategory.getImageUrl(),
                                       updatedCategory.getDescription());
    }
}
