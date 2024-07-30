package gift.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PostMapping
    public CategoryResponseDto addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.postCategory(categoryRequestDto);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto updateCateory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.putCategory(id, categoryRequestDto);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<String> deleteCategry(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("성공적으로 삭제되었습니다");
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

}
