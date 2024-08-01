package gift.product.category.controller;

import gift.product.category.dto.request.CreateCategoryRequest;
import gift.product.category.dto.request.UpdateCategoryRequest;
import gift.product.category.dto.response.CategoryResponse;
import gift.product.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "카테고리 관리", description = "카테고리를 관리하는 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "모든 카테고리를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "카테고리가 존재하지 않음",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("{id}")
    @Operation(summary = "특정 카테고리 조회", description = "id를 통해 특정 카테고리를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "해당 id를 가진 카테고리가 존재하지 않음")
    })
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",
            headers = {@Header(name = "location", description = "카테고리 생성 위치 엔드포인트")}),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 오류 발생")
    })
    public ResponseEntity<CategoryResponse> addCategory(
        @RequestBody @Valid CreateCategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        URI location = UriComponentsBuilder.fromPath("/api/categories/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location)
            .body(response);
    }

    @PatchMapping("{id}")
    @Operation(summary = "카테고리 수정", description = "{id}의 카테고리를 수정합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "{id}의 상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 오류 발생")
    })
    public ResponseEntity<Void> updateCategory(@PathVariable Long id,
        @RequestBody @Valid UpdateCategoryRequest request) {
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "카테고리 삭제", description = "{id}의 카테고리를 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "{id}의 상품을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}
