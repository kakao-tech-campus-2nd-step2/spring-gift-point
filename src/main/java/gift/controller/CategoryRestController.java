package gift.controller;

import gift.dto.request.CategoryRequest;
import gift.dto.response.CategoryResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "새로운 카테고리를 추가합니다")
    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        categoryService.save(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "ID로 카테고리를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(id));
    }

    @Operation(summary = "모든 카테고리를 조회합니다")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    @Operation(summary = "ID로 카테고리를 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "ID로 카테고리 정보를 업데이트합니다")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategoryById(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest){
        categoryService.updateById(id,categoryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
