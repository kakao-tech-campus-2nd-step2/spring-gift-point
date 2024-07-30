package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Category;
import gift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ApiResponse<List<Category>> response = new ApiResponse<>(true, "Categories retrieved successfully", categories, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        ApiResponse<Category> response = new ApiResponse<>(true, "Category created successfully", createdCategory, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        try {
            Category category = categoryService.updateCategory(id, updatedCategory);
            ApiResponse<Category> response = new ApiResponse<>(true, "Category updated successfully", category, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            ApiResponse<Category> response = new ApiResponse<>(false, "Category not found", null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            ApiResponse<Void> response = new ApiResponse<>(true, "Category deleted successfully", null, null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Category not found", null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}