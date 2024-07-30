package gift.service;

import gift.entity.Category;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findByCategoryName(String name) {
        return categoryRepository.findByName(name);
    }

    public void save(Category category) {
        if (findByCategoryName(category.getName()).isPresent()) {
            throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }
        categoryRepository.save(category);
    }

    public void delete(Long categoryId) {
        categoryRepository.delete(findById(categoryId));
    }

    public void update(Long categoryId, Category category) {
        Category update = findById(categoryId);
        update.setName(category.getName());
        categoryRepository.save(update);
    }

}
