package gift.controller;

import gift.dto.response.CategoryResponse;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "[협업] CATEGORY API", description= "[협업] 카테고리 컨트롤러")
public class CategoryNewController {

    private final CategoryService categoryService;

    public CategoryNewController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "카테고리 목록 조회합니다.")
    public ResponseEntity<List<CategoryResponse>> getCategories(){
        List<CategoryResponse> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }
}
