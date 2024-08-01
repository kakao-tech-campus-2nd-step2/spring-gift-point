package gift.controller.api;

import gift.dto.request.AddCategoryRequest;
import gift.dto.request.UpdateCategoryRequest;
import gift.dto.response.CategoryIdResponse;
import gift.dto.response.CategoryResponse;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryIdResponse> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        CategoryIdResponse categoryIdResponse = categoryService.addCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryIdResponse);
    }

    @GetMapping("/api/categories")
    public ResponseEntity<Page<CategoryResponse>> getCategories(Pageable pageable) {
        Page<CategoryResponse> allCategories = categoryService.getAllCategoryResponsesByPageable(pageable);
        return ResponseEntity.ok(allCategories);
    }

    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> updateCategory(@Valid @RequestBody UpdateCategoryRequest request, @PathVariable("categoryId") Long categoryId) {
        categoryService.updateCategory(request, categoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
