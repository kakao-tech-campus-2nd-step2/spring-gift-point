package gift.domain.category;

import gift.global.exception.category.CategoryDuplicateException;
import gift.global.exception.category.CategoryNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final JpaCategoryRepository categoryRepository;

    public CategoryService(gift.domain.category.JpaCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.name())) {
            throw new CategoryDuplicateException(categoryDTO.name());
        }

        Category category = new Category(categoryDTO.name(), categoryDTO.description());
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundException(id);
        }

        categoryRepository.deleteById(id);
    }

    @Transactional
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        Category findCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));

        // 이름 중복 검사, 중복되면서 id 가 자신이 아닐 때
        if (hasDuplicateName(id, categoryDTO.name())) {
            throw new CategoryDuplicateException(categoryDTO.name());

        }
        findCategory.update(categoryDTO.name(), categoryDTO.description());
    }

    /**
     * @param id   수정할 카테고리 ID
     * @param name 수정할 카테고리 이름
     */
    private boolean hasDuplicateName(Long id, String name) {
        return categoryRepository
            .findByName(name)
            .filter(c -> !c.getId().equals(id))
            .isPresent();
    }
}
