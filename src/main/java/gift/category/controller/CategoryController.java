package gift.category.controller;

import gift.category.model.Category;
import gift.category.service.CategoryService;
import gift.common.util.CommonResponse;
import gift.product.model.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. 카테고리 생성
    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 생성 성공", true));
    }

    // 2. 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        categoryService.updateCategory(categoryId, category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 수정 성공", true));
    }

    // 3. 카테고리에 저장된 상품들 전부 조회
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductDTO> productDTOList = categoryService.getAllProducts();
        return ResponseEntity.ok(new CommonResponse<>(productDTOList, "카테고리에 저장된 상품들 조회 성공", true));
    }
}
