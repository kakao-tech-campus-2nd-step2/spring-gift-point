package gift.controller;

import gift.dto.CategoryDTO;
import gift.entity.Category;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category(카테고리)", description = "Category관련 API입니다.")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "전체 Category 목록 조회", description = "저장된 모든 카테고리의 정보를 가져옵니다.")
    public ResponseEntity<List<Category>> getAllCategory() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Category 추가", description = "새 카테고리를 추가합니다.")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryDTO.toEntity();
        categoryService.save(category);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

}
