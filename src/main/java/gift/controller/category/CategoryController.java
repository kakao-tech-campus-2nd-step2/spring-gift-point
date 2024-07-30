package gift.controller.category;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.controller.auth.Token;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Category", description = "Category API")
@RequestMapping("/api/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "get All Categories", description = "모든 카테고리 불러오기(기본 개수 : 5개)")
    public ResponseEntity<ApiResponseBody<List<CategoryResponse>>> getAllCategories() {
        return new ApiResponseBuilder<List<CategoryResponse>>()
            .httpStatus(HttpStatus.OK)
            .data(categoryService.findAll())
            .messages("모든 카테고리 조회")
            .build();
    }

    @PostMapping
    @Operation(summary = "create Cateogory", description = "카테고리 생성")
    public ResponseEntity<ApiResponseBody<CategoryResponse>> createCategory(@LoginUser LoginResponse member, @RequestBody CategoryRequest category) {
        return new ApiResponseBuilder<CategoryResponse>()
            .httpStatus(HttpStatus.OK)
            .data(categoryService.save(category))
            .messages("카테고리 생성")
            .build();
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "modify Category", description = "카테고리 변경")
    public ResponseEntity<ApiResponseBody<CategoryResponse>> updateCategory(@LoginUser LoginResponse member,
        @PathVariable UUID categoryId, @RequestBody CategoryRequest category) {
        return new ApiResponseBuilder<CategoryResponse>()
            .httpStatus(HttpStatus.OK)
            .data(categoryService.update(categoryId, category))
            .messages("카테고리 수정")
            .build();
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete Category", description = "카테고리 삭제")
    public ResponseEntity<ApiResponseBody<Void>> deleteCategory(@LoginUser LoginResponse member,
        @PathVariable UUID categoryId) {
        categoryService.delete(categoryId);
        return new ApiResponseBuilder<Void>()
            .httpStatus(HttpStatus.OK)
            .data(null)
            .messages("카테고리 삭제")
            .build();
    }
}