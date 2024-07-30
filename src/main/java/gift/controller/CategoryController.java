package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
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
@Tag(name = "Category", description = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        String category = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("추가된 카테고리: " + category);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    public ResponseEntity<CategoryDto> updateProduct(@PathVariable("categoryId") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제한다.")
    public ResponseEntity<String> deleteWish(@PathVariable("categoryId") Long categoryId) {
        String category = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제된 카테고리: " + category);
    }

}
