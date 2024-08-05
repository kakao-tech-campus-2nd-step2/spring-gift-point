package gift.service.category;

import gift.dto.category.CategoryRequest;
import gift.dto.category.CategoryResponse;
import gift.exception.EntityNotFoundException;
import gift.model.category.Category;
import gift.repository.category.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(CategoryRequest categoryRequest) {
        Category category = categoryRequest.toEntity();
        categoryRepository.save(category);
    }

    public CategoryResponse.Info getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다. id :  " + categoryId));
        return CategoryResponse.Info.fromEntity(category);

    }

    public CategoryResponse.InfoList getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryResponse.InfoList.fromEntity(categories);
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다. id :  " + categoryId));
        category.modify(
                categoryRequest.getName(),
                categoryRequest.getColor(),
                categoryRequest.getImageUrl(),
                categoryRequest.getDescription());
        categoryRepository.save(category);

    }


    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
