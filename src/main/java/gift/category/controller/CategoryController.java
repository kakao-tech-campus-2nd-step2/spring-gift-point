package gift.category.controller;

import gift.category.domain.Category;
import gift.category.dto.CategoryListDTO;
import gift.category.dto.CategoryRequest;
import gift.category.service.CategoryService;
import gift.common.util.CommonResponse;
import gift.product.domain.ProductDTO;
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
    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.getName(), categoryRequest.getColor(), categoryRequest.getImageUrl(), categoryRequest.getDescription());
        categoryService.createCategory(category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 생성 성공", true));
    }

    // 2. 카테고리 수정
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.getName(), categoryRequest.getColor(), categoryRequest.getImageUrl(), categoryRequest.getDescription());
        categoryService.updateCategory(categoryId, category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 수정 성공", true));
    }

    // 3. 카테고리 목록 조회
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<CategoryListDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new CommonResponse<>(categories, "카테고리 목록 조회 성공", true));
    }

    // 4. 카테고리에 저장된 상품들 전부 조회
    @Operation(summary = "카테고리에 저장된 상품들 전부 조회", description = "카테고리에 저장된 상품들 전부 조회한다.")
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        List<ProductDTO> productDTOList = categoryService.getAllProducts();
        return ResponseEntity.ok(new CommonResponse<>(productDTOList, "카테고리에 저장된 상품들 조회 성공", true));
    }
}
