package gift.category.controller;

import gift.category.domain.Category;
import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryListResponseDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.service.CategoryService;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import jakarta.validation.Valid;
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

    @GetMapping("")
    public ResponseEntity<CategoryListResponseDto> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        CategoryListResponseDto categoryListResponseDto = new CategoryListResponseDto(categories);
        return ResponseEntity.status(200)
                .body(categoryListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable(name = "id") Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);
        return ResponseEntity.status(200)
                .body(categoryResponseDto);
    }

    @PostMapping("")
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(categoryRequestDto.toCategoryServiceDto());
        return ResponseEntity.status(200)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.updateCategory(categoryRequestDto.toCategoryServiceDto(id));
        return ResponseEntity.status(200)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(200)
                .build();
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}