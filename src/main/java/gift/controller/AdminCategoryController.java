package gift.controller;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "카테고리 관련 API")
public class AdminCategoryController {

    private CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 생성 성공"),
        @ApiResponse(responseCode = "-40001", description = "요청 시 만족해야 하는 제약 조건을 위반하는 경우"),
        @ApiResponse(responseCode = "-40902", description = "카테고리 추가/수정 시 이름이 기존 카테고리와 중복되는 경우")
    })
    public ResponseEntity<Map<String, CategoryResponse>> addCategory(
        @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        Map<String, CategoryResponse> response = new HashMap<>();
        response.put("category", categoryResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "-40402", description = "해당 카테고리를 찾을 수 없음"),
        @ApiResponse(responseCode = "-40902", description = "카테고리 추가/수정 시 이름이 기존 카테고리와 중복되는 경우")
    })
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId,
        @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(categoryId, categoryRequest);
        return ResponseEntity.noContent().build();
    }

}
