package gift.controller;

import gift.dto.CategoryRequest;
import gift.dto.ResponseMessage;
import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("api/categories")
@Tag(name = "Category Management", description = "APIs for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.",
        responses = @ApiResponse(responseCode = "201", description = "카테고리 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))))
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest request) {
        Category category = categoryService.addCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카테고리 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))))
    public Page<Category> getCategory(
        @ParameterObject @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "ID로 카테고리 조회", description = "카테고리 ID에 해당하는 카테고리를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카테고리 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))))
    public Category getOneCategory(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 업데이트", description = "카테고리 ID에 해당하는 카테고리를 업데이트합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카테고리 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))))
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId,
        @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리 ID에 해당하는 카테고리를 삭제합니다.",
        responses = @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))))
    public ResponseEntity<ResponseMessage> deleteCategory(
        @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        ResponseMessage responseMessage = new ResponseMessage("삭제되었습니다.");
        return ResponseEntity.ok(responseMessage);
    }
}
