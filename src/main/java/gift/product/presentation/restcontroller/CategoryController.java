package gift.product.presentation.restcontroller;

import gift.docs.product.CategoryApiDocs;
import gift.product.business.service.CategoryService;
import gift.product.presentation.dto.CategoryRequest;
import gift.product.presentation.dto.ResponseCategoryDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApiDocs {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseCategoryDto>> getCategories() {
        var categoryDtos = categoryService.getCategories();
        var responseCategoryDtos = ResponseCategoryDto.of(categoryDtos);
        return ResponseEntity.ok(responseCategoryDtos);
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
        @Valid @RequestBody List<CategoryRequest.Create> categoryRequestCreates) {
        var categoryInCreates = categoryRequestCreates.stream()
            .map(CategoryRequest.Create::toCategoryInCreate)
            .toList();
        categoryService.createCategory(categoryInCreates);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(@RequestParam("id") Long id,
        @Valid @RequestBody CategoryRequest.Update categoryRequestUpdate) {
        var categoryInUpdate = categoryRequestUpdate.toCategoryInUpdate(id);
        var updatedCategoryId = categoryService.updateCategory(categoryInUpdate);
        return ResponseEntity.ok(updatedCategoryId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCategory(@RequestParam("id") Long id) {
        var deletedCategoryId = categoryService.deleteCategory(id);
        return ResponseEntity.ok(deletedCategoryId);
    }


}
