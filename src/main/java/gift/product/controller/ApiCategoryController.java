package gift.product.controller;

import gift.product.dto.CategoryDTO;
import gift.product.model.Category;
import gift.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "API 컨트롤러")
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public ApiCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
        summary = "카테고리 등록",
        description = "상품의 분류를 정하는 카테고리를 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "카테고리 등록 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Category.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "등록을 시도한 카테고리의 이름 누락"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "등록을 시도한 카테고리 이름이 기등록된 카테고리의 이름과 중복"
        )
    })
    @PostMapping
    public ResponseEntity<Category> registerCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        System.out.println("[CategoryController] registerCategory()");
        Category category = categoryService.registerCategory(categoryDTO.convertToDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(
        summary = "카테고리 이름 수정",
        description = "카테고리의 이름을 수정합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "카테고리 수정 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Category.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "수정을 시도한 카테고리의 이름 누락"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "등록되지 않은 카테고리에 대한 수정 시도"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "수정을 시도한 카테고리 이름이 기등록된 카테고리의 이름과 중복(또는 기존과 동일한 이름으로 수정 시도)"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        System.out.println("[CategoryController] updateCategory()");
        Category category = categoryService.updateCategory(categoryDTO.convertToDomain(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(
        summary = "카테고리 삭제",
        description = "상품의 분류를 정하는 카테고리를 삭제합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "카테고리 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "등록되지 않은 카테고리에 대한 삭제 시도"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        System.out.println("[CategoryController] deleteCategory()");
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
