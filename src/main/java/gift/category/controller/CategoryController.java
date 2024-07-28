package gift.category.controller;

import gift.category.model.Category;
import gift.category.service.CategoryService;
import gift.common.util.CommonResponse;
import gift.product.model.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. 카테고리 생성
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 생성 성공", true));
    }

    // 2. 카테고리 수정
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        categoryService.updateCategory(id, category);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 수정 성공", true));
    }

    // 3. 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok(new CommonResponse<>(null, "카테고리 삭제 성공", true));
    }

    // 4. 카테고리에 저장된 상품들 전부 조회
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<ProductDTO> productDTOList = categoryService.getAllProducts();
        return ResponseEntity.ok(new CommonResponse<>(productDTOList, "카테고리에 저장된 상품들 조회 성공", true));
    }
}
