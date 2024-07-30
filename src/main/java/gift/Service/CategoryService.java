package gift.Service;

import gift.DTO.CategoryDTO;
import gift.Model.Category;
import gift.Repository.CategoryRepository;
import gift.Repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId){
        return categoryRepository.findCategoryById(categoryId);
    }

    public Category addCategory(CategoryDTO categoryDTO){
        Category category = new Category(categoryDTO.getId(),categoryDTO.getName(),categoryDTO.getColor(),categoryDTO.getImageUrl(),categoryDTO.getDescription(),new ArrayList<>());
        return categoryRepository.save(category);
    }

    public Category updateCategory(CategoryDTO categoryDTO){
        Category category = new Category(categoryDTO.getId(), categoryDTO.getName(),categoryDTO.getColor(),categoryDTO.getImageUrl(),categoryDTO.getDescription(),categoryDTO.getProducts());
        return categoryRepository.save(category);
    }

    public Category deleteCategory(Long categoryId){
        productRepository.deleteByCategoryId(categoryId);
        Category deleteCategories = categoryRepository.findCategoryById(categoryId);
        categoryRepository.deleteById(categoryId);
        return deleteCategories;
    }
}
