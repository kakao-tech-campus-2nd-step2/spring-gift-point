package gift.category;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponse addCategory(@RequestBody CategoryRequest newCategory){
        return categoryService.insertNewCategory(newCategory);
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories(){
        return categoryService.findAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryByID(@PathVariable Long id){
        return categoryService.findCategoriesByID(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategories(@PathVariable Long id, @RequestBody CategoryRequest categoryDTO){
        return categoryService.updateCategoriesByID(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategories(@PathVariable Long id){
        categoryService.deleteCategoriesByID(id);

    }


}
