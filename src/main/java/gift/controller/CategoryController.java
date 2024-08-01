package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "카테고리 관련 API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.",
            requestBody = @RequestBody(
                    description = "추가할 카테고리의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CategoryRequestDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리가 성공적으로 추가되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = CategoryResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 카테고리 이름이 유효하지 않은 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<CategoryResponseDto> addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.addCategory(categoryRequestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "수정할 카테고리 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @RequestBody(
                    description = "수정할 카테고리의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CategoryRequestDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 수정되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = CategoryResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryRequestDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = CategoryResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "카테고리 조회", description = "ID로 카테고리를 조회합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "조회할 카테고리 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리가 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = CategoryResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "기존 카테고리를 삭제합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "삭제할 카테고리 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리가 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
