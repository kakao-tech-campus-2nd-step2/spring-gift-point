package gift.controller;

import gift.dto.CategoryResponseDto;
import gift.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDto = categoryService.getAllCategoryResponseDto();
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<CategoryResponseDto> getCategoryDtoByProductId(@RequestParam("product_id") Long productId) {
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryDtoByProductId(productId);
        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }
}
