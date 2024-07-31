package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CategoryController", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
  @PostMapping
  public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
    CategoryDto savedCategoryDto = categoryService.saveCategory(categoryDto);
    return new ResponseEntity<>(savedCategoryDto, HttpStatus.CREATED);
  }

  @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<CategoryDto> categories = categoryService.findAllCategories();
    return ResponseEntity.ok(categories);
  }
}