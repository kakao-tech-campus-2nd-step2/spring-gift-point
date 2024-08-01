package gift.controller;

import gift.model.Category;
import gift.service.CategoryService;
import gift.service.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if (categoryService.existsCategory(category.getName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists");
        }
        categoryService.addCategory(category);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("category_id") long id) {
        if (!categoryService.existsCategoryById(id)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category not found!");
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
