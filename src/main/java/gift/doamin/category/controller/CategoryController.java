package gift.doamin.category.controller;

import gift.doamin.category.dto.CategoryForm;
import gift.doamin.category.dto.CategoryParam;
import gift.doamin.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리")
@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "새 카테고리 등록", description = "관리자가 새로운 카테고리를 등록할 때 사용하는 API")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void createNewCategory(@RequestBody CategoryForm categoryForm) {
        categoryService.createCategory(categoryForm);
    }

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리들의 정보를 조회합니다.")
    @GetMapping
    public List<CategoryParam> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "단일 카테고리 조회", description = "선택한 카테고리 하나의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public CategoryParam getOneCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @Operation(summary = "카테고리 이름 변경", description = "관리자가 카테고리 이름을 변경할때 사용하는 API")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changeCategoryName(@RequestBody CategoryForm categoryForm, @PathVariable Long id) {
        categoryForm.setId(id);
        categoryService.updateCategory(categoryForm);
    }

    @Operation(summary = "카테고리 삭제", description = "관리자가 카테고리를 삭제할 때 사용하는 API")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
