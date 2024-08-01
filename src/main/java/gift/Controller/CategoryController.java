package gift.Controller;

import gift.Model.CategoryDto;
import gift.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "카테고리 관련 api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryDtoList);
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping("/")
    public ResponseEntity<?> addCategory(CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(null,null,201);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable long categoryId, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        categoryService.updateCategory(categoryDto);
        return ResponseEntity.ok("카테고리 정보를 수정했습니다.");
    }
}
