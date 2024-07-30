package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.CategoryRequest;
import gift.main.entity.Category;
import gift.main.repository.CategoryRepository;
import gift.main.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> getCategoryAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void addCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new CustomException(ErrorCode.ALREADY_CATEGORY_NAME);
        }
        if (categoryRepository.existsByUniNumber(categoryRequest.uniNumber())) {
            throw new CustomException(ErrorCode.ALREADY_CATEGORY_UNI_NUMBER);
        }
        Category category = new Category(categoryRequest);

        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        if (productRepository.existsByCategoryId(id)) {

            throw new CustomException(ErrorCode.EXISTS_PRODUCT);
        }
        categoryRepository.delete(category);
    }

    @Transactional
    public void updateCategory(Long categoryid, CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new CustomException(ErrorCode.ALREADY_CATEGORY_NAME);
        }
        if (categoryRepository.existsByUniNumber(categoryRequest.uniNumber())) {
            throw new CustomException(ErrorCode.ALREADY_CATEGORY_UNI_NUMBER);
        }
        Category category = categoryRepository.findById(categoryid)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));
        category.updateCategory(categoryRequest);
    }

}
