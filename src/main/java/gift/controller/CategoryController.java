package gift.controller;

import gift.dto.request.CategoryRequestDTO;
import gift.dto.response.CategoryResponseDTO;
import gift.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<CategoryResponseDTO> categoryList = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryList);
    }

    @PostMapping("")
    public ResponseEntity<String> createCategory (@RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryService.addCategory(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Category created");
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory (@PathVariable("categoryId") Long categoryId) {
        categoryService.removeCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Category deleted");
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<String> updateCategory (@PathVariable("categoryId") Long categoryId,
                                                  @RequestBody CategoryRequestDTO categoryRequestDTO) {
        categoryService.updateCategory(categoryId, categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Category updated");
    }



}