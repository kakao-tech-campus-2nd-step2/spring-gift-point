package gift.category.controller;

import gift.category.dto.CategoryRequestDto;
import gift.category.dto.CategoryResponseDto;
import gift.category.service.CategoryService;
import gift.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "카테고리 관련 API")
public class CategoryController extends ResponseUtil {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
  public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    List<CategoryResponseDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @PostMapping
  @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
  public ResponseEntity<CategoryResponseDto> createCategory(
      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto);
    return new ResponseEntity<>(categoryResponseDto, HttpStatus.CREATED);
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDto> getCategory(
      @PathVariable Long categoryId) {
    CategoryResponseDto responseDto = categoryService.getCategory(categoryId);
    return ResponseEntity.ok(responseDto);
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryResponseDto> updateCategory(
      @PathVariable Long categoryId,
      @Valid @RequestBody CategoryRequestDto requestDto) {
    CategoryResponseDto responseDto = categoryService.updateCategory(categoryId, requestDto);
    return ResponseEntity.ok(responseDto);
  }


  @DeleteMapping("/{categoryId}")
  @Operation(summary = "카테고리 삭제", description = "기존 카테고리를 삭제한다.")
  public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable Long categoryId) {
    CategoryResponseDto deletedCategory = categoryService.deleteCategory(categoryId);
    return successResponse(deletedCategory);
  }

}
