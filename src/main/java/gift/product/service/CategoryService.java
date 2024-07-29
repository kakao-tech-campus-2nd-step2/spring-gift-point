package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.exception.InvalidIdException;
import gift.product.model.Category;
import gift.product.repository.CategoryRepository;
import gift.product.validation.CategoryValidation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidation categoryValidation;

    @Autowired
    public CategoryService(
        CategoryRepository categoryRepository,
        CategoryValidation categoryValidation
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryValidation = categoryValidation;
    }

    public Category registerCategory(Category category) {
        System.out.println("[CategoryService] registerCategory()");
        categoryValidation.registerCategory(category);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {
        System.out.println("[CategoryService] updateCategory()");
        categoryValidation.updateCategory(category);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        System.out.println("[CategoryService] deleteCategory()");
        categoryValidation.deleteCategory(id);
        categoryRepository.deleteById(id);
    }

    public Page<Category> findAllCategory(Pageable pageable) {
        System.out.println("[CategoryService] getAllCategories()");
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        System.out.println("[CategoryService] findCategoryById()");
        return categoryRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }
}
