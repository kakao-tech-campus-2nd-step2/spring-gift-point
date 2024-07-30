package gift.services;

import gift.domain.Category;
import gift.dto.CategoryDto;
import gift.dto.RequestCategoryDto;
import gift.repositories.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(category -> new CategoryDto(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
            ))
            .collect(Collectors.toList());
    }

    public void addCategory(RequestCategoryDto requestCategoryDto) {
        Category category = new Category(requestCategoryDto.getName(),
            requestCategoryDto.getColor(), requestCategoryDto.getImageUrl(),
            requestCategoryDto.getDescription());
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Category not found with id" + id));
        categoryRepository.deleteById(id);
    }
}
