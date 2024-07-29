package gift.controller;

import gift.model.CategoryDTO;
import gift.model.CategoryPageDTO;
import gift.model.ProductDTO;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
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
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 페이지 조회", description = "쿼리 스트링으로 오프셋 페이지네이션을 지원합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CategoryPageDTO.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터입니다.")
    })
    public ResponseEntity<?> getCategoryPage(Pageable pageable) {
        CategoryPageDTO categoryPage = categoryService.findCategoryPage(pageable);
        return ResponseEntity.ok(categoryPage.categories());
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "특정 카테고리 조회", description = "카테고리 ID를 통해 특정 카테고리를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "잘못된 카테고리 ID 를 입력했습니다.")
    })
    public ResponseEntity<?> getCategory(
        @Parameter(description = "카테고리 ID", example = "1", required = true)
        @PathVariable long categoryId) {
        CategoryDTO categoryDTO = categoryService.findCategoryById(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/{categoryId}/products")
    @Operation(summary = "특정 카테고리 내 상품 조회", description = "카테고리 ID를 통해 해당 카테고리 내 모든 상품을 조회합니다."
        + "쿼리 스트링으로 페이지네이션을 지원합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "404", description = "잘못된 카테고리 ID 를 입력했습니다.")
    })
    public ResponseEntity<?> getProductsInCategory(Pageable pageable,
        @Parameter(description = "카테고리 ID", example = "1", required = true)
        @PathVariable long categoryId) {
        List<ProductDTO> products = categoryService.findProductsInCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    public ResponseEntity<?> createCategory(
        @Parameter(description = "생성한 카테고리 정보", required = true)
        @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 정보 수정", description = "카테고리 ID를 통해 특정 카테고리 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
    })
    public ResponseEntity<?> updateCategory(
        @Parameter(description = "카테고리 ID", example = "1", required = true)
        @PathVariable long categoryId,
        @Parameter(description = "수정할 카테고리 정보", required = true)
        @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리 ID를 통해 특정 카테고리를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "성공적으로 카테고리를 삭제했습니다."),
        @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다.")
    })
    public ResponseEntity<?> deleteCategory(
        @Parameter(description = "카테고리 ID", example = "1", required = true)
        @PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}