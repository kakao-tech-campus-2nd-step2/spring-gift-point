package gift.controller.rest;

import gift.entity.category.Category;
import gift.entity.category.CategoryDTO;
import gift.entity.category.CategoryRequest;
import gift.entity.response.MessageResponseDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category 컨트롤러", description = "Category API입니다.")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping()
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
    @PostMapping()
    public ResponseEntity<Category> postCategory(@RequestBody CategoryRequest form) {
        Category result = categoryService.save(form);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 조회", description = "id로 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO result = categoryService.findOne(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 편집", description = "id로 카테고리를 편집합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Category> putCategory(@PathVariable Long id, @RequestBody CategoryRequest form) {
        Category result = categoryService.update(id, form);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "카테고리 삭제", description = "id로 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity
                .ok()
                .body(new MessageResponseDTO("deleted successfully"));
    }
}
