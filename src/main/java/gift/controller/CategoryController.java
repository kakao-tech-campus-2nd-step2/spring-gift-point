package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        String category = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("추가된 카테고리: " + category);
    }

    @PostMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateProduct(@PathVariable("categoryId") Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteWish(@PathVariable("categoryId") Long categoryId) {
        String category = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body("삭제된 카테고리: " + category);
    }

}
