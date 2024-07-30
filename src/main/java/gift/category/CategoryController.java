package gift.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "category", description = "카테고리 관련 API")
@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 조회", description = "모든 카테고리 조회")
    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "카테고리 추가", description = "카테고리를 생성합니다.")
    @PostMapping()
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "카테고리 변경", description = "카테고리 속성 변경")
    @PutMapping()
    public ResponseEntity<Category> updateCategory(@RequestBody CategoryRequest categoryRequest){
        Category category = categoryService.updateCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제 합니다.")
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteCategory(@RequestParam Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
