package gift.main.controller;

import gift.main.dto.CategoryRequest;
import gift.main.entity.Category;
import gift.main.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategorylist() {
        List<Category> categoryList = categoryService.getCategoryAll();
        return ResponseEntity.ok(categoryList);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok("Category added successfully");
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "id") Long id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok("Category updated successfully");
    }


}
