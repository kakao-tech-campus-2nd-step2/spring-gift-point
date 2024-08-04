package gift.category.service;

import gift.category.entity.Category;
import gift.category.dto.CategoryDto;
import gift.category.exception.NoSuchCategoryException;
import gift.category.repository.CategoryRepository;
import java.util.List;
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

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll()
            .stream()
            .map(category -> category.toDto())
            .collect(Collectors.toList());
    }

    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(NoSuchCategoryException::new)
            .toDto();
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        return categoryRepository.save(categoryDto.toEntity()).toDto();
    }

    public CategoryDto updateCategory(long id, CategoryDto categoryDto) {
        getCategory(id);
        Category category = new Category(id, categoryDto.name(), categoryDto.imageUrl(), categoryDto.description());
        return categoryRepository.save(category).toDto();
    }

    public CategoryDto deleteCategory(long id) {
        Category deletedCategory = categoryRepository.findById(id).orElseThrow(NoSuchCategoryException::new);
        categoryRepository.delete(deletedCategory);
        return deletedCategory.toDto();
    }
}
