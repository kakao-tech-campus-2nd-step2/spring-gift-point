package gift.controller;

import gift.model.Category;
import gift.service.CategoryService;
import gift.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if (categoryService.existsCategory(category.getCategoryName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }


    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@RequestBody Category category) {
        if (!categoryService.existsCategory(category.getCategoryName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category not found!");
        }
        categoryService.deleteCategory(category.getId());
        return ResponseEntity.ok().body("Category deleted successfully");
    }

}
