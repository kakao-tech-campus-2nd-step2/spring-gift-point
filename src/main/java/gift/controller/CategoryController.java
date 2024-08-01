package gift.controller;

import gift.model.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CategoryController", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
  @PostMapping
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    Category createdCategory = categoryService.createCategory(category);
    return ResponseEntity.ok(createdCategory);
  }

  @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
  @PutMapping("/{categoryId}")
  public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category categoryDetails) {
    Category updatedCategory = categoryService.updateCategory(categoryId, categoryDetails);
    return ResponseEntity.ok(updatedCategory);
  }

  @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
}