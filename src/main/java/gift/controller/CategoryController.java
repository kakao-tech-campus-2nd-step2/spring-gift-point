package gift.controller;


import gift.DTO.CategoryDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "카테고리 api", description = "카테고리와 관련된 api 입니다")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리 정보를 가져옵니다.")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
