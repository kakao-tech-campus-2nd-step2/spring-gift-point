package gift.service;

import gift.controller.dto.CategoryRequest;
import gift.controller.dto.CategoryResponse;
import gift.domain.Category;
import gift.repository.CategoryRepository;
import gift.utils.error.CategoryNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<CategoryResponse> findAllCategory(){
        List<CategoryResponse> categories = categoryRepository.findAll().stream().map(
            category -> new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImageUrl(),
                category.getDescription()
            )
        ).toList();
        return categories;
    }
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        Category category = new Category(categoryRequest.getName(), categoryRequest.getColor(),
            categoryRequest.getImageUrl(), categoryRequest.getDescription());
        Category save = categoryRepository.save(category);
        return new CategoryResponse(save.getId(),save.getName(), save.getColor(), save.getImageUrl(),
            save.getDescription());
    }
    @Transactional
    public void updateCategory(Long id, CategoryRequest categoryRequest){
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new CategoryNotFoundException("Category Not Found")
        );
        category.updateCategory(categoryRequest);

    }
    @Transactional
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
            () -> new CategoryNotFoundException("Category Not Found")
        );
        categoryRepository.deleteById(id);
    }
}
