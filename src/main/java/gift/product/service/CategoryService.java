package gift.product.service;

import com.sun.jdi.request.DuplicateRequestException;
import gift.product.dto.category.CategoryDto;
import gift.product.model.Category;
import gift.product.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategoryAll() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return getExistenceValidatedCategory(id);
    }

    @Transactional
    public Category insertCategory(CategoryDto categoryDto) {
        ValidateRedundancyCategory(categoryDto.name());

        Category category = new Category(categoryDto.name(), categoryDto.color(),
            categoryDto.imageUrl(), categoryDto.description());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryDto categoryDto) {
        getExistenceValidatedCategory(id);

        Category category = new Category(id, categoryDto.name(), categoryDto.color(),
            categoryDto.imageUrl(), categoryDto.description());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        getExistenceValidatedCategory(id);

        categoryRepository.deleteById(id);
    }

    private Category getExistenceValidatedCategory(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 카테고리가 존재하지 않습니다."));
    }

    private void ValidateRedundancyCategory(String name) {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);

        if (categoryOptional.isPresent()) {
            throw new DuplicateRequestException("이미 존재하는 카테고리입니다.");
        }
    }
}
