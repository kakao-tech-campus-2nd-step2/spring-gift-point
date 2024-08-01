package gift.controller;

import gift.domain.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "카테고리 API 관련 엔드포인트")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 전체 조회", description = "모든 카테고리의 목록을 조회합니다.")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존의 카테고리 정보를 수정합니다.")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "기존의 카테고리를 삭제합니다.")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("카테고리 삭제 완료!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
