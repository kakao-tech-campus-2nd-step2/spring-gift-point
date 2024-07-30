package gift.category;

import gift.category.model.CategoryRequest;
import gift.category.model.CategoryResponse;
import gift.common.model.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category API", description = "category 를 관리하는 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponseDto<CategoryResponse>> getAllCategories(
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(
            PageResponseDto.of(categoryService.getAllCategories(pageable), pageable));
    }

    @Operation(summary = "특정 카테고리 조회", description = "id에 해당하는 카테고리를 조회합니다.")
    @Parameter(name = "id", description = "조회할 카테고리 id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<Void> addCategory(
        @Valid @RequestBody CategoryRequest categoryRequest) {
        Long id = categoryService.insertCategory(categoryRequest);
        return ResponseEntity.created(URI.create("/api/categories/" + id)).build();
    }

    @Operation(summary = "특정 카테고리 수정", description = "id에 해당하는 카테고리를 수정합니다.")
    @Parameter(name = "id", description = "수정할 카테고리 id")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
        @Valid @RequestBody CategoryRequest categoryRequest,
        @PathVariable(name = "id") Long id) {
        categoryService.updateCategory(categoryRequest, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 카테고리 삭제", description = "id에 해당하는 카테고리를 삭제합니다.")
    @Parameter(name = "id", description = "삭제할 카테고리 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
