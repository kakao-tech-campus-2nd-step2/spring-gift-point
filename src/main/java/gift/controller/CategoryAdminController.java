package gift.controller;

import gift.domain.AppUser;
import gift.dto.category.CategoryRequest;
import gift.service.CategoryService;
import gift.util.aspect.AdminController;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminController
@RequestMapping("/api/admin/categories")
@Tag(name = "Category", description = "Category Admin API")
public class CategoryAdminController {
    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "관리자 권한으로 카테고리 추가")
    @PostMapping
    public ResponseEntity<String> addCategoryForAdmin(@LoginUser AppUser loginAppUser,
                                                      @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @Operation(summary = "관리자 권한으로 카테고리 수정")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoryForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                         @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "관리자 권한으로 카테고리 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("ok");
    }
}
