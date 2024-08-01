package gift.controller;

import gift.dto.category.CategoryCreateRequest;
import gift.dto.category.CategoryResponse;
import gift.dto.category.CategoryUpdateRequest;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category API", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "(명세 통일) 모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "카테고리 조회", description = "ID를 사용하여 특정 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
        @Valid @RequestBody CategoryCreateRequest categoryCreateRequest
    ) {
        CategoryResponse createdCategory = categoryService.addCategory(categoryCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest
    ) {
        CategoryResponse updatedCategory = categoryService.updateCategory(
            id,
            categoryUpdateRequest
        );
        return ResponseEntity.ok(updatedCategory);
    }
}
