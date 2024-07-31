package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    @Operation(summary = "카테고리 저장", description = "카테고리를 저장합니다.")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        String category = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("추가된 카테고리: " + category);
    }

    @PostMapping("/update/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    public ResponseEntity<CategoryDto> updateProduct(@PathVariable("categoryId") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping("/delete/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<String> deleteWish(@PathVariable("categoryId") Long categoryId) {
        String category = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제된 카테고리: " + category);
    }

}
