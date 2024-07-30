package gift.domain.category;

import gift.domain.category.dto.request.CategoryRequest;
import gift.global.response.ResponseMaker;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category API")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성")
    public ResponseEntity createCategories(
        @Valid @RequestBody List<CategoryRequest> categoryRequests) {
        categoryService.createCategories(categoryRequests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{categoryId}")
    @Operation(summary = "카테고리 삭제")
    public ResponseEntity deleteCategory(
        @Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{categoryId}")
    @Operation(summary = "카테고리 수정")
    public ResponseEntity updateCategory(
        @Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long id,
        @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok().build();
    }
}
