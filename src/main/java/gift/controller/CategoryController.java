package gift.controller;

import gift.domain.category.Category;
import gift.domain.category.CategoryRequest;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> readCategories(
        @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
        @RequestParam(required = false, defaultValue = "10", value = "size") int pageSize) {
        return categoryService.findAll(pageNo, pageSize);
    }

    @GetMapping("/{categoryId}")
    public Category readCategory(@PathVariable Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @PostMapping
    public Category createCategory(
        @Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.save(categoryRequest);
    }

    @PutMapping("/{categoryId}")
    public Category updateCategory(@PathVariable Long categoryId,
        @Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.update(categoryId, categoryRequest);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }
}
