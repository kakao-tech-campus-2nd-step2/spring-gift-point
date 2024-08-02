package gift.Controller;

import gift.Model.request.CategoryRequest;
import gift.Model.response.CategoryResponse;
import gift.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리 API", description = "카테고리 관련된 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 목록 조회", description = "category 데이터베이스에 있는 데이터를 조회합니다.")
    @GetMapping
    public List<CategoryResponse> read(){
        return categoryService.read();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok("성공");
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> update(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest){
        categoryService.update(categoryId, categoryRequest);
        return ResponseEntity.ok("성공");
    }
}
