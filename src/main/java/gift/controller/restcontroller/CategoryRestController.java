package gift.controller.restcontroller;

import gift.controller.dto.request.CategoryRequest;
import gift.controller.dto.response.CategoryResponse;
import gift.controller.dto.response.ProductResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "카테고리 API")
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryRestController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @PostMapping("/category")
    @Operation(summary = "카테고리 저장", description = "카테고리를 저장합니다.")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCategory(
            @Valid @RequestBody CategoryRequest.CreateCategory request
    ) {
        Long id = categoryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/categories")
    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> responses = categoryService.getAllCategories();
        return ResponseEntity.ok().body(responses);
    }

    @PutMapping("/category")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    public ResponseEntity<Void> updateCategory(
            @Valid @RequestBody CategoryRequest.UpdateCategory request
    ) {
        categoryService.updateById(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/category/{id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("id") @NotNull @Min(1) Long id
    ) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{id}/products")
    @Operation(summary = "카테고리에 해당하는 상품 조회", description = "카테고리에 해당하는 상품을 조회합니다.")
    public ResponseEntity<List<ProductResponse.Info>> getProductsByCategoryId(
        @PathVariable("id") @NotNull @Min(1) Long id
    ) {
        List<ProductResponse.Info> responses = productService.findProductsByCategoryId(id);
        return ResponseEntity.ok().body(responses);
    }
}
