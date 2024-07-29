package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import gift.vo.Category;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    @Operation(
            summary = "전체 카테고리 조회",
            description = "모든 카테고리를 조회하는 API입니다."
    )
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = service.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(
            summary = "카테고리 생성",
            description = "새로운 카테고리를 생성하는 API입니다."
    )
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto) {
        service.addCategory(categoryDto.toCategory());
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    @Operation(
            summary = "카테고리 업데이트",
            description = "카테고리를 업데이트하는 API입니다."
    )
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDto categoryDto) {
        service.updateCategory(categoryDto.toCategory());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "카테고리 삭제",
            description = "주어진 ID에 해당하는 카테고리를 삭제하는 API입니다."
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id") Long id) {
        service.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
