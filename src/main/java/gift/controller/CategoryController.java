package gift.controller;

import gift.dto.request.CategoryRequest;
import gift.dto.response.CategoryResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> responseBody = categoryService.getCategories();
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/{id}")
    @Operation(summary = "카테고리 조회", description = "특정 카테고리를 조회한다.")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        CategoryResponse responseBody = categoryService.getCategory(id);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse responseBody = categoryService.createCategory(categoryRequest);
        return ResponseEntity.ok().body(responseBody);
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse responseBody = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "기존 카테고리를 삭제한다.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

