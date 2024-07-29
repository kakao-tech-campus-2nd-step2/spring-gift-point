package gift.domain.category.controller;

import gift.domain.category.dto.CategoryRequest;
import gift.domain.category.dto.CategoryResponse;
import gift.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "CategoryController", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    @Operation(summary = "카테고리 전체 조회", description = "카테고리 전체를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
        @Parameter(name = "pageNo", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "pageSize", description = "페이지 크기", example = "10")
    })
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<CategoryResponse> categoryPage = categoryService.getAllCategories(pageNo, pageSize);
        return ResponseEntity.ok(categoryPage);
    }

    @PostMapping()
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "해당 카테고리를 수정합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "수정할 카테고리 id", example = "1")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") Long id,
        @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "해당 카테고리를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "id", description = "삭제할 카테고리 id", example = "1")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
