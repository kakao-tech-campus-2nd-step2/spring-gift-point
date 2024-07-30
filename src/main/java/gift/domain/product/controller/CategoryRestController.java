package gift.domain.product.controller;

import gift.domain.product.dto.CategoryResponse;
import gift.domain.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 API")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "모든 상품 카테고리를 조회합니다.")
    public ResponseEntity<List<CategoryResponse>> readAll() {
        List<CategoryResponse> categories = categoryService.readAll();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}
