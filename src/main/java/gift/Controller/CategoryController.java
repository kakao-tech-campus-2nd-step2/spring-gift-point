package gift.Controller;

import gift.DTO.CategoryDto;
import gift.Service.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<CategoryDto> categoryDtos = categoryService.getAllCategories();
    return ResponseEntity.ok(categoryDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
    CategoryDto categoryDto = categoryService.getCategoryById(id);
    return ResponseEntity.ok(categoryDto);
  }

  @PostMapping
  public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
    CategoryDto addedCategoryDto = categoryService.addCategory(categoryDto);
    var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
      .buildAndExpand(addedCategoryDto).toUri();

    return ResponseEntity.created(location).body(addedCategoryDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
    @RequestBody CategoryDto categoryDto) {
    CategoryDto updatedCategoryDto = categoryService.updateCategory(id, categoryDto);
    return ResponseEntity.ok(updatedCategoryDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
    CategoryDto categoryDto = categoryService.deleteCategory(id);

    return ResponseEntity.ok(categoryDto);
  }
}
