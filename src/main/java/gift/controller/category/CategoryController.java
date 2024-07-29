package gift.controller.category;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Category", description = "Category API")
@RequestMapping("/api/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "get All Categories", description = "모든 카테고리 불러오기(기본 개수 : 5개)")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

    @PostMapping
    @Operation(summary = "create Cateogory", description = "카테고리 생성")
    public ResponseEntity<CategoryResponse> createCategory(@LoginUser LoginResponse member, @RequestBody CategoryRequest category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "modify Category", description = "카테고리 변경")
    public ResponseEntity<CategoryResponse> updateCategory(@LoginAdmin LoginResponse member,
        @PathVariable UUID categoryId, @RequestBody CategoryRequest category) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(categoryService.update(categoryId, category));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete Category", description = "카테고리 삭제")
    public ResponseEntity<Void> deleteCategory(@LoginAdmin LoginResponse member,
        @PathVariable UUID categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}