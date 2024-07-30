package gift.controller;

import gift.dto.CategoryDto;
import gift.model.Category;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Category", description = "카테고리 관련 api")
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 id로 카테고리 조회", description = "카테고리 id로 카테고리를 조회합니다.")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "카테고리를 추가합니다.")
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.addCategory(categoryDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{categoryId}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(location).body(category);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        productService.updateProductCategoryToNone(categoryId);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
