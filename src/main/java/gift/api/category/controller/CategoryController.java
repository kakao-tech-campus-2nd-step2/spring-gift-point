package gift.api.category.controller;

import gift.api.category.dto.CategoryRequest;
import gift.api.category.dto.CategoryResponse;
import gift.api.category.service.CategoryService;
import gift.global.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Category", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    @Operation(summary = "카테고리 조회", description = "카테고리 전체 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<ListResponse<CategoryResponse>> getCategories(
        @Parameter(deprecated = true)
        @RequestParam(required = false) @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {

        return ResponseEntity.ok().body(ListResponse.of(categoryService.getCategories(pageable)));
    }

    @PostMapping
    @Operation(summary = "카테고리 추가")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<Void> add(
        @Parameter(required = true, description = "카테고리 요청 본문")
        @RequestBody CategoryRequest categoryRequest) {

        return ResponseEntity.created(
            URI.create("/api/categories/" + categoryService.add(categoryRequest))).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    public ResponseEntity<Void> update(
        @Parameter(required = true, description = "수정할 카테고리 ID")
        @PathVariable("id") Long id,
        @Parameter(required = true, description = "카테고리 요청 본문")
        @RequestBody CategoryRequest categoryRequest) {

        categoryService.update(id, categoryRequest);
        return ResponseEntity.ok().build();
    }
}
