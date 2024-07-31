package gift.product.application;

import gift.auth.interceptor.Authorized;
import gift.member.domain.Role;
import gift.product.application.dto.request.CategoryRequest;
import gift.product.application.dto.response.CategoryPageResponse;
import gift.product.application.dto.response.CategoryResponse;
import gift.product.service.CategoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiResponse(responseCode = "201", description = "카테고리 등록 성공")
    @PostMapping()
    @Authorized(Role.USER)
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequest request) {
        var savedCategoryId = categoryService.createCategory(request.toCategoryParam());

        return ResponseEntity.created(URI.create("/api/categories/" + savedCategoryId))
                .build();
    }

    @ApiResponse(responseCode = "200", description = "카테고리 수정 성공")
    @PutMapping("/{id}")
    @Authorized(Role.USER)
    @ResponseStatus(HttpStatus.OK)
    public void modifyCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        categoryService.modifyCategory(id, request.toCategoryParam());
    }

    @ApiResponse(responseCode = "200", description = "카테고리 조회 성공")
    @GetMapping("/{id}")
    @Authorized(Role.USER)
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        var categoryInfo = categoryService.getCategoryInfo(id);

        var response = CategoryResponse.from(categoryInfo);
        return ResponseEntity.ok()
                .body(response);
    }

    @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    @GetMapping()
    @Authorized(Role.USER)
    public ResponseEntity<CategoryPageResponse> getCategoryList(Pageable pageable) {
        var categoryList = categoryService.getCategoryList(pageable);

        var response = CategoryPageResponse.from(categoryList);
        return ResponseEntity.ok()
                .body(response);
    }

    @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공")
    @DeleteMapping("/{id}")
    @Authorized(Role.USER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
