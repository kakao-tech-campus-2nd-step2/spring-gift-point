package gift.controller;

import gift.domain.AppUser;
import gift.dto.category.CategoryRequest;
import gift.dto.common.CommonResponse;
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
    public ResponseEntity<?> addCategoryForAdmin(@LoginUser AppUser loginAppUser,
                                                      @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(null, "관리자 권한으로 카테고리 추가가 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 카테고리 수정")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                         @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 카테고리 수정이 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 카테고리 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 카테고리 삭제가 완료되었습니다.", true));
    }
}
