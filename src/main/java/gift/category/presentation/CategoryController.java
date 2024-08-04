package gift.category.presentation;

import gift.category.application.dto.CategoryRequestDto;
import gift.category.application.dto.CategoryResponseDto;
import gift.category.application.service.CategoryService;
import java.util.List;
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
    public void deleteCategry(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

}
