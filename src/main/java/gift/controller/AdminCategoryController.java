package gift.controller;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.entity.Role;
import gift.service.CategoryService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
        @ApiResponse(responseCode = "201", description = "카테고리 추가 성공")
    })
    public ResponseEntity<CategoryResponse> addCategory(
        @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
    })
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
        @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse updatedCategory = categoryService.updateCategory(categoryId, categoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

}
