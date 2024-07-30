package gift.product.presentation;

import gift.product.application.CategoryService;
import gift.product.domain.CreateCategoryRequest;
import gift.util.CommonResponse;
import gift.util.annotation.AdminAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CategoryController", description = "카테고리 관리 관련 API")
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(new CommonResponse<>(
                categoryService.getCategory(),
                "카테고리 조회 성공",
                true
        ));
    }

    @AdminAuthenticated
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @PostMapping("/create")
    public ResponseEntity<?> addCategory(@RequestBody @Validated CreateCategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok(new CommonResponse<>(
                null,
                "카테고리 추가 성공",
                true
        ));
    }
}
