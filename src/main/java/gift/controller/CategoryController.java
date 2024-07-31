package gift.controller;

import gift.dto.category.CategoryResponse;
import gift.dto.common.CommonResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category User API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Id로 카테고리 조회")
    @GetMapping("/{id}")
    public ResponseEntity<?> findCategoryById(
            @Parameter(description = "ID of the category to be searched", required = true)
            @PathVariable Long id) {
        CategoryResponse response = categoryService.findCategoryById(id);
        return ResponseEntity.ok(new CommonResponse<>(response, "카테고리 조회가 완료되었습니다.", true));
    }

    @Operation(summary = "카테고리 전체 조회")
    @GetMapping
    public ResponseEntity<?> findAllCategories() {
        List<CategoryResponse> response = categoryService.findAllCategories();
        return ResponseEntity.ok(new CommonResponse<>(response, "카테고리 전체 조회가 완료되었습니다.", true));
    }
}
