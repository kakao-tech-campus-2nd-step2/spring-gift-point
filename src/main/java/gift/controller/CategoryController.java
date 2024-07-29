package gift.controller;

import gift.dto.CategoryRequest;
import gift.model.Category;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/categories/")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return ResponseEntity.ok().body(categories);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid CategoryRequest request) {
        categoryService.update(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
