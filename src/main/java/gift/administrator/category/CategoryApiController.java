package gift.administrator.category;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Category API", description = "category related API")
public class CategoryApiController {

    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "get all categories", description = "모든 카테고리를 조회합니다.")
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>(HttpResult.OK,
            "카테고리 전체 조회 성공", HttpStatus.OK, categories);
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "get one category", description = "카테고리 하나를 카테고리 아이디로 조회합니다.")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(
        @PathVariable("categoryId") Long categoryId) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "카테고리 조회 성공",
            HttpStatus.OK, categoryDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    @Operation(summary = "add one category", description = "카테고리를 하나 추가합니다. name(required), color, "
        + "imageUrl, description")
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(
        @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.existsByNameThrowException(categoryDTO.getName());
        CategoryDTO result = categoryService.addCategory(categoryDTO);
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "카테고리 추가 성공",
            HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "update one category", description =
        "카테고리 아이디로 카테고리 하나를 수정합니다. name(required), "
            + "color, imageUrl, description")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
        @PathVariable("categoryId") Long categoryId,
        @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.existsByNameAndId(categoryDTO.getName(), categoryId);
        CategoryDTO result = categoryService.updateCategory(categoryDTO, categoryId);
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "카테고리 수정 성공",
            HttpStatus.OK, result);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "delete one category", description = "카테고리 아이디로 카테고리 하나를 삭제합니다.")
    public ResponseEntity<ApiResponse<CategoryDTO>> deleteCategory(
        @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "카테고리 삭제 성공",
            HttpStatus.OK, null);
        return ResponseEntity.ok(apiResponse);
    }
}
