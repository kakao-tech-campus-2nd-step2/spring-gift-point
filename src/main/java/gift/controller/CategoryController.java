package gift.controller;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomArgumentNotValidException;
import gift.exception.customException.CustomDuplicateException;
import gift.exception.customException.CustomException;
import gift.model.categories.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category Api")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "카테고리 추가.")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryForm,
        BindingResult result)
        throws CustomException, CustomArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.INVALID_INPUT);
        }
        if (categoryService.isDuplicateName(categoryForm.getName())) {
            result.rejectValue("name", "", ErrorCode.DUPLICATE_NAME.getMessage());
            throw new CustomDuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        return ResponseEntity.ok(categoryService.insertCategory(categoryForm));
    }

    @Operation(summary = "카테고리 목록 조회.")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategoryList());
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
        @Parameter(name = "id", description = "수정할 카테고리의 id") @PathVariable("id") Long id,
        @Valid @RequestBody CategoryDTO categoryForm, BindingResult result)
        throws CustomException, CustomArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.INVALID_INPUT);
        }
        if (categoryService.isDuplicateName(categoryForm.getName())) {
            result.rejectValue("name", "", ErrorCode.DUPLICATE_NAME.getMessage());
            throw new CustomDuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        CategoryDTO updated = categoryService.updateCategory(
            new CategoryDTO(id, categoryForm.getName(), categoryForm.getImgUrl(),
                categoryForm.getDescription()));
        return ResponseEntity.ok(updated);
    }
}
