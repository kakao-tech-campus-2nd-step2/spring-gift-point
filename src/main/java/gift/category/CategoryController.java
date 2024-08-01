package gift.category;

import gift.category.dto.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "Category API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public List<CategoryResponseDTO> getCategories() {
        return categoryService.getAllCategories();
    }
}
