package gift.product.business.service;

import gift.product.business.dto.CategoryDto;
import gift.product.business.dto.CategoryIn;
import gift.product.persistence.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void createCategory(List<CategoryIn.Create> categoryInCreates) {
        var categories = categoryInCreates.stream()
            .map(CategoryIn.Create::toCategory)
            .toList();
        categoryRepository.saveCategories(categories);
    }

    @Transactional
    public Long updateCategory(CategoryIn.Update categoryInUpdate) {
        var category = categoryRepository.getCategory(categoryInUpdate.id());
        category.update(
            categoryInUpdate.name(),
            categoryInUpdate.description(),
            categoryInUpdate.color(),
            categoryInUpdate.imageUrl()
        );
        return categoryRepository.saveCategory(category);
    }

    public Long deleteCategory(Long id) {
        return categoryRepository.deleteCategory(id);
    }
}
