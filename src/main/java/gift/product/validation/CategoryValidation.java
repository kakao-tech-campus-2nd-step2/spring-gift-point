package gift.product.validation;

import static gift.product.exception.GlobalExceptionHandler.DUPLICATE_CATEGORY_NAME;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.USING_CATEGORY;

import gift.product.exception.DuplicateException;
import gift.product.exception.InvalidIdException;
import gift.product.model.Category;
import gift.product.repository.CategoryRepository;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryValidation {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CategoryValidation(
        ProductRepository productRepository,
        CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public void registerCategory(Category category) {
        validateDuplicateName(category.getName());
    }

    public void updateCategory(Category category) {
        validateExistId(category.getId());
        validateDuplicateName(category.getName());
    }

    public void deleteCategory(Long id) {
        validateExistId(id);
        if(productRepository.findByCategoryId(id).isPresent())
            throw new InvalidIdException(USING_CATEGORY);
    }

    private void validateDuplicateName(String name) {
        if(categoryRepository.findByName(name).isPresent())
            throw new DuplicateException(DUPLICATE_CATEGORY_NAME);
    }

    private void validateExistId(Long id) {
        if(!categoryRepository.existsById(id))
            throw new InvalidIdException(NOT_EXIST_ID);
    }

}
