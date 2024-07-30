package gift.service;

import gift.entity.Category;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.CategoryRepository;
import java.util.List;
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

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void delete(Long categoryId){
        categoryRepository.delete(findById(categoryId));
    }

    public void update(Long categoryId, Category category){
        Category update = findById(categoryId);
        update.setName(category.getName());
        categoryRepository.save(update);
    }

}
