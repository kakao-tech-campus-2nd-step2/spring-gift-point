package gift.category.controller;

import gift.category.domain.Category;
import gift.category.dto.CategoryRequestDto;
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
    public ResponseEntity<ResultResponseDto<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseHelper.createResponse(ResultCode.GET_ALL_PRODUCTS_SUCCESS, categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<Category>> getCategoryById(@PathVariable(name = "id") Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseHelper.createResponse(ResultCode.GET_PRODUCT_BY_ID_SUCCESS, category);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.createCategory(categoryRequestDto.toCategoryServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_PRODUCT_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateProduct(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        categoryService.updateCategory(categoryRequestDto.toCategoryServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_PRODUCT_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteProduct(@PathVariable(name = "id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_PRODUCT_SUCCESS);
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