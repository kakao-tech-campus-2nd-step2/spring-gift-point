package gift.service;

import gift.dto.CategoryResponseDTO;
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

    public CategoryResponseDTO getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return new CategoryResponseDTO(categoryList);
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                                 .orElseThrow(CustomException::categoryNotFoundException);
    }
}
