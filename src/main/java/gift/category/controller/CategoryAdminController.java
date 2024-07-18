package gift.category.controller;

import gift.category.model.dto.CategoryRequest;
import gift.category.service.CategoryService;
import gift.user.model.dto.AppUser;
import gift.user.resolver.LoginUser;
import gift.user.service.UserService;
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
@RequestMapping("/api/category/admin")
public class CategoryAdminController {
    private final UserService userService;
    private final CategoryService categoryService;

    public CategoryAdminController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<String> addCategoryForAdmin(@LoginUser AppUser loginAppUser,
                                                      @Valid @RequestBody CategoryRequest categoryRequest) {
        userService.verifyAdminAccess(loginAppUser);
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoryForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                         @Valid @RequestBody CategoryRequest categoryRequest) {
        userService.verifyAdminAccess(loginAppUser);
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok().body("ok");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        userService.verifyAdminAccess(loginAppUser);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("ok");
    }
}
