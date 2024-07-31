package gift.api.category.controller;

import gift.api.category.dto.CategoryRequest;
import gift.api.category.dto.CategoryResponse;
import gift.api.category.service.CategoryService;
import gift.global.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    @Operation(summary = "카테고리 조회", description = "카테고리 페이지별 조회")
    public ResponseEntity<ListResponse<CategoryResponse>> getCategories(
        @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return ResponseEntity.ok().body(ListResponse.of(categoryService.getCategories(pageable)));
    }

    @PostMapping
    @Operation(summary = "카테고리 추가")
    public ResponseEntity<Void> add(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.created(
            URI.create("/api/categories/" + categoryService.add(categoryRequest))).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "카테고리 수정")
    public ResponseEntity<Void> update(@PathVariable("id") Long id,
        @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(id, categoryRequest);
        return ResponseEntity.ok().build();
    }
}
