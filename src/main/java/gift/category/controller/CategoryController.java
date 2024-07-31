package gift.category.controller;

import gift.category.dto.CategoryRequest;
import gift.category.dto.CategoryResponse;
import gift.category.model.Category;
import gift.category.service.CategoryService;
import gift.dto.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories() {
        var allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(
            new CategoryResponse(HttpResult.OK, "카테고리 전체 조회 성공",
                HttpStatus.OK,
                allCategories
            ));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(
        @PathVariable("categoryId") Long id) {
        var foundCategory = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
            new CategoryResponse(HttpResult.OK, "카테고리 검색 성공", HttpStatus.OK, foundCategory));
    }


    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(
        @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok(
            new CategoryResponse(HttpResult.OK,
                "카테고리 추가 성공",
                HttpStatus.OK,
                category
            ));
    }



    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategoryName(
        @PathVariable(value = "categoryId") Long categoryId,
        @RequestBody CategoryRequest categoryRequest) {
        var updatedCategory = categoryService.updateCategory(categoryId,categoryRequest);
        return ResponseEntity.ok(
            new CategoryResponse(HttpResult.OK, "카테고리 수정 성공", HttpStatus.OK, updatedCategory));
    }

    @DeleteMapping
    public ResponseEntity<CategoryResponse> deleteCategoryById(@RequestParam("id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(
            new CategoryResponse(
                HttpResult.OK,
                "카테고리 삭제 성공",
                HttpStatus.OK,
                null
            ));
    }
}