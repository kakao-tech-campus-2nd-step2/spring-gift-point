package gift.controller;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok().body(categoryService.getCategories());
    }

    @Operation(summary = "한 카테고리 조회", description = "해당 id의 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(categoryService.getCategory(id));
    }

    @Operation(summary = "카테고리 추가", description = "카테고리를 추가합니다.")
    @PostMapping
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO addedcategoryDTO = categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok().body(addedcategoryDTO);
    }

    @Operation(summary = "카테고리 수정", description = "해당 id의 카테고리를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok().body(categoryService.updateCategory(id, categoryDTO));
    }

    @Operation(summary = "카테고리 삭제", description = "해당 id의 카테고리를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));
    }
}
