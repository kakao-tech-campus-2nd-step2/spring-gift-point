package gift.product.presentation.restcontroller;

import gift.product.business.service.CategoryService;
import gift.product.presentation.dto.RequestCategoryDto;
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
public class CategoryController {

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
    public ResponseEntity<Long> createCategory(@Valid @RequestBody RequestCategoryDto requestCategoryDto) {
        var categoryRegisterDto = requestCategoryDto.toCategoryRegisterDto();
        var createdCategoryId = categoryService.createCategory(categoryRegisterDto);
        return ResponseEntity.ok(createdCategoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(@RequestParam("id") Long id,
        @Valid @RequestBody RequestCategoryDto requestCategoryDto) {
        var categoryUpdateDto = requestCategoryDto.toCategoryUpdateDto(id);
        var updatedCategoryId = categoryService.updateCategory(categoryUpdateDto);
        return ResponseEntity.ok(updatedCategoryId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCategory(@RequestParam("id") Long id) {
        var deletedCategoryId = categoryService.deleteCategory(id);
        return ResponseEntity.ok(deletedCategoryId);
    }



}
