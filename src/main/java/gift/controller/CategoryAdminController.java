package gift.controller;

import gift.controller.dto.CategoryRequest;
import gift.controller.dto.CategoryResponse;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest
        categoryRequest){
        CategoryResponse category = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Null> updateCategory(@RequestParam Long id, @Valid
    @RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCategory(@RequestParam Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(id);
    }
}
