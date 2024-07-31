package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.dto.ErrorResponseDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 정보를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 조회 성공", content = @Content(schema = @Schema(implementation = CategoryResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryList = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<String> addCategory(
        @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        categoryService.addCategory(categoryRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 ID를 기반으로 특정 카테고리를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
        @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        categoryService.updateCategory(id, categoryRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 ID를 기반으로 특정 카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}