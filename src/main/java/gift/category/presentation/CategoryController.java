package gift.category.presentation;

import gift.category.application.CategoryService;
import gift.category.presentation.request.CategoryCreateRequest;
import gift.category.presentation.request.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApi{

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CategoryCreateRequest request
    ) {
        Long categoryId = categoryService.create(request.toCommand());

        return ResponseEntity.created(URI.create("/api/categories/" + categoryId)).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryControllerResponse>> findAll() {
        return ResponseEntity.ok(categoryService.findAll().stream().map(CategoryControllerResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryControllerResponse> findById(
            @PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(CategoryControllerResponse.from(categoryService.findById(categoryId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long categoryId,
            @RequestBody CategoryUpdateRequest request
    ) {
        categoryService.update(request.toCommand(categoryId));

        return ResponseEntity.created(URI.create("/api/categories/" + categoryId)).build();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long categoryId
    ) {
        categoryService.delete(categoryId);
    }
}
