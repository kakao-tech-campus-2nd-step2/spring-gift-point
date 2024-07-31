package gift.controller;

import gift.domain.Category;
import gift.domain.CategoryRequest;
import gift.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.create(categoryRequest);
        return ResponseEntity.ok().body("successfully created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.update(categoryRequest);
        return ResponseEntity.ok().body("successfully created");
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategoryList(){
        return ResponseEntity.ok().body(categoryService.getCategotyList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable("id") Long id
    ){
        categoryService.deleteById(id);
        return ResponseEntity.ok().body("successfully deleted");
    }
}
