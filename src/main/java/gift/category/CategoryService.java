package gift.category;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryRequest categoryRequest) {
        return categoryRepository.save(categoryRequest.toEntity());
    }

    public Category updateCategory(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.getId()).orElseThrow();
        category.update(categoryRequest.getName(), categoryRequest.getColor(),
            categoryRequest.getImageUrl(), categoryRequest.getDescription());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
