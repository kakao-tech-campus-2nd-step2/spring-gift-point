package gift.controller;

import gift.dto.CategoryDTO;
import gift.dto.ErrorResponse;
import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@Tag(name = "Category(카테고리)", description = "Category관련 API입니다.")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "전체 Category 목록 조회", description = "저장된 모든 카테고리의 정보를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 오류.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),})
    public ResponseEntity<List<Category>> getAllCategory() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Category 추가", description = "새 카테고리를 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "카테고리 추가 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "카테고리 이름 중복 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryDTO.toEntity();
        categoryService.save(category);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PutMapping({"/{categoryId}"})
    @Operation(summary = "Category 수정", description = "기존의 카테고리를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 수정 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "수정하려는 카테고리 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "카테고리 이름 중복 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<String> updateCategory(@RequestBody CategoryDTO categoryDTO,
        @Parameter(name = "categoryId", description = "수정할 카테고리 ID")
        @PathVariable("categoryId") Long categoryId) {
        Category category = categoryDTO.toEntity();
        categoryService.update(categoryId, category);
        return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/{categoryId}"})
    @Operation(summary = "Category 삭제", description = "기존의 카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "삭제하려는 카테고리 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return new ResponseEntity<>("OK", HttpStatus.NO_CONTENT);
    }
}
