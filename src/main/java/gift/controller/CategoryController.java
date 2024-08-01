package gift.controller;

import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="카테고리 API")
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 프론트 요청 : response(카테고리 정보 리스트 리턴해주세요)
    @Operation(summary = "카테고리 목록 조회")
    @GetMapping
    public List<Category> getCategories() {
        return categoryService.findAll();
    }
}