package gift.category.controller;

import gift.category.dto.CategoryReqDto;
import gift.category.dto.CategoryResDto;
import gift.category.service.CategoryService;
import gift.common.annotation.AllowAnonymous;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@Tag(name = "카테고리 API", description = "카테고리를 조회, 추가, 수정, 삭제하는 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공"),
            })
    @AllowAnonymous
    public ResponseEntity<List<CategoryResDto>> getCategories() {
        List<CategoryResDto> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "카테고리를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "카테고리 추가 성공"),
            })
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryReqDto categoryReqDto) {
        categoryService.addCategory(categoryReqDto);
        return ResponseEntity.created(URI.create("/api/categories")).body("카테고리를 추가했습니다.");
    }

    @PutMapping("/{category-id}")
    @Operation(summary = "카테고리 수정", description = "카테고리 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            })
    public ResponseEntity<String> updateCategory(
            @PathVariable("category-id") Long categoryId,
            @Valid @RequestBody CategoryReqDto categoryReqDto
    ) {
        categoryService.updateCategory(categoryId, categoryReqDto);
        return ResponseEntity.ok("카테고리 정보를 수정했습니다.");
    }

    @DeleteMapping("/{category-id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            })
    public ResponseEntity<String> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("카테고리를 삭제했습니다.");
    }
}
