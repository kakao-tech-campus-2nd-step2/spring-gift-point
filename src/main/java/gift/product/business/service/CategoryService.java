package gift.product.business.service;

import gift.product.business.dto.CategoryDto;
import gift.product.business.dto.CategoryRegisterDto;
import gift.product.business.dto.CategoryUpdateDto;
import gift.product.persistence.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getCategories() {
        var categories = categoryRepository.getAllCategories();
        return CategoryDto.of(categories);
    }

    public Long createCategory(CategoryRegisterDto categoryRegisterDto) {
        var category = categoryRegisterDto.toCategory();
        return categoryRepository.saveCategory(category);
    }

    public Long updateCategory(CategoryUpdateDto categoryRegisterDto) {
        var category = categoryRepository.getCategory(categoryRegisterDto.id());
        category.setName(categoryRegisterDto.name());
        return categoryRepository.saveCategory(category);
    }

    public Long deleteCategory(Long id) {
        return categoryRepository.deleteCategory(id);
    }
}
