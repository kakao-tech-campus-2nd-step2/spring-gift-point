package gift.controller;

import gift.entity.Category;
import gift.exception.CustomException;
import gift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management System", description = "Operations related to category management")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "View a list of available categories", description = "Provides a list of all available categories", tags = { "Category Management System" })
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by Id", description = "Provides details of a category by ID", tags = { "Category Management System" })
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID of the category to be fetched", required = true)
            @PathVariable Long id) {
        Category category = categoryService.getCategoryById(id).orElseThrow(() -> new CustomException.EntityNotFoundException("Category not found"));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Add a new category", description = "Adds a new category to the system", tags = { "Category Management System" })
    public ResponseEntity<Category> addCategory(
            @Parameter(description = "Category object to be added", required = true)
            @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category", description = "Updates details of an existing category", tags = { "Category Management System" })
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "ID of the category to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated category object", required = true)
            @RequestBody Category categoryDetails) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category from the system", tags = { "Category Management System" })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to be deleted", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
