package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public void saveCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.saveCategory(categoryDTO);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategory(){
        return categoryService.getAllCategories();
    }
}
