package gift.category.presentation;

import gift.category.application.CategoryService;
import gift.category.presentation.request.CategoryCreateRequest;
import gift.category.presentation.request.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApi{

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public void create(
            @RequestBody CategoryCreateRequest request
    ) {
        categoryService.create(request.toCommand());
    }

    @GetMapping("")
    public ResponseEntity<Page<CategoryControllerResponse>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(categoryService.findAll(pageable).map(CategoryControllerResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryControllerResponse> findById(
            @PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(CategoryControllerResponse.from(categoryService.findById(categoryId)));
    }

    @PutMapping("/{id}")
    public void update(
            @PathVariable("id") Long categoryId,
            @RequestBody CategoryUpdateRequest request
    ) {
        categoryService.update(request.toCommand(categoryId));
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long categoryId
    ) {
        categoryService.delete(categoryId);
    }
}
