package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import gift.vo.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "카테고리 관리", description = "카테고리 조회, 생성, 업데이트, 삭제와 관련된 API들을 제공합니다.")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    @Operation(
            summary = "전체 카테고리 조회",
            description = "모든 카테고리를 조회하는 API입니다."
    )
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> categories = service.getCategories();

        return ResponseEntity.ok().body(categories.stream().map(CategoryDto::fromCategory).toList());
    }


    @PostMapping()
    @Operation(
            summary = "카테고리 생성",
            description = "새로운 카테고리를 생성하는 API입니다."
    )
    @Parameter(name = "categoryDto", description = "생성할 카테고리 정보를 포함하는 Dto", required = true)
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDto categoryDto) {
        service.addCategory(categoryDto.toCategory());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping()
    @Operation(
            summary = "카테고리 업데이트",
            description = "카테고리를 업데이트하는 API입니다."
    )
    @Parameter(name = "categoryDto", description = "업데이트할 카테고리 정보 Dto", required = true)
    public ResponseEntity<Void> updateCategory(@RequestBody CategoryDto categoryDto) {
        service.updateCategory(categoryDto.toCategory());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "카테고리 삭제",
            description = "주어진 ID에 해당하는 카테고리를 삭제하는 API입니다."
    )
    @Parameter(name = "id", description = "삭제할 카테고리의 ID", required = true, example = "1")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value = "id") Long id) {
        service.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
