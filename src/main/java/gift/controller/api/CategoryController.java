package gift.controller.api;

import gift.dto.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    public List<CategoryDTO> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/web")
    @Operation(summary = "웹용 카테고리 조회", description = "웹에서 사용할 모든 카테고리를 조회합니다.")
    public List<CategoryDTO> getCategoriesForWeb() {
        return categoryService.getAllCategories();
    }
}
