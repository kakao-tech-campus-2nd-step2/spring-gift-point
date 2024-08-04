package gift.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "category", description = "카테고리 관련 API")
@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 조회", description = "모든 카테고리 조회")
    @GetMapping()
    public ResponseEntity<CategoryContents> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(CategoryContents.from(categoryService.getAllCategories()));
    }

    @Operation(summary = "카테고리 추가", description = "카테고리를 생성합니다.")
    @PostMapping()
    public ResponseEntity<CategoryResponse> addCategory(
        @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryResponse(category));
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 속성 변경")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
        @RequestBody CategoryRequest categoryRequest, @PathVariable("categoryId") Long id) {
        Category category = categoryService.updateCategory(categoryRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(new CategoryResponse(category));
    }

    // Delete 사용 안함
    /*@Operation(summary = "카테고리 삭제", description = "카테고리를 삭제 합니다.")
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteCategory(@RequestParam Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }*/
}
