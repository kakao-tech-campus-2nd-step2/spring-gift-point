package gift.controller;

import gift.dto.CategoryDto;
import gift.dto.request.CategoryRequest;
import gift.dto.response.CategoryResponse;
import gift.dto.response.ErrorResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category API", description = "카테고리 관련 API")
@RequestMapping("/api/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> categoryList() {
        List<CategoryDto> categoryDtoList = categoryService.getCategories();
        List<CategoryResponse> categoryResponseList = categoryDtoList.stream()
                .map(CategoryDto::toResponseDto)
                .toList();

        return ResponseEntity.ok()
                .body(categoryResponseList);
    }

    @Operation(summary = "새 카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름으로 인한 추가 실패"),
    })
    @PostMapping
    public ResponseEntity<Void> categoryAdd(@RequestBody @Valid CategoryRequest request) {
        CategoryDto categoryDto = new CategoryDto(request.getName());

        categoryService.addCategory(categoryDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "기존 카테고리 수정", description = "카테고리 ID를 사용하여 기존 카테고리 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름으로 인한 수정 실패"),
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> categoryEdit(@Parameter(description = "수정할 카테고리의 ID", required = true) @PathVariable Long categoryId,
                                             @RequestBody @Valid CategoryRequest request) {
        CategoryDto categoryDto = new CategoryDto(request.getName());
        categoryService.editCategory(categoryId, categoryDto);

        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 ID를 사용하여 카테고리를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> categoryRemove(@Parameter(description = "삭제할 카테고리의 ID", required = true) @PathVariable Long categoryId) {
        categoryService.removeCategory(categoryId);

        return ResponseEntity.ok()
                .build();
    }

}
