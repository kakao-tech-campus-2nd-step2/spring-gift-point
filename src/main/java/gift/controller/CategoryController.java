package gift.controller;

import gift.dto.SuccessResponse;
import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 생성 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> saveCategory(@RequestBody CategoryRequestDto request) {
        categoryService.saveCategory(request);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.OK, "카테고리가 성공적으로 등록되었습니다."));
    }

    @Operation(summary = "카테고리 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        List<CategoryResponseDto> result = categoryService.getAll();
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 개별 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable("id") Long id) {
        CategoryResponseDto result = categoryService.getSingleCategory(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> editCategory(@PathVariable("id") Long id,
                                                            @RequestBody CategoryRequestDto request) {
        CategoryResponseDto result = categoryService.editCategory(id, request);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "20", description = "카테고리 삭제 성공"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body(new SuccessResponse(HttpStatus.NO_CONTENT, "성공적으로 삭제되었습니다."));
    }
}
