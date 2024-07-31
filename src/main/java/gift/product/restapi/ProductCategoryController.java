package gift.product.restapi;

import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductCategoryService;
import gift.product.restapi.dto.request.CategoryCreateRequest;
import gift.product.restapi.dto.request.CategoryUpdateRequest;
import gift.product.restapi.dto.response.PagedCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "상품 카테고리")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    @Operation(
            summary = "카테고리 목록 조회",
            description = "카테고리 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (기본 값 : 0)"),
                    @Parameter(name = "size", description = "페이지 크기 (기본 값 : 10)")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리 목록을 조회합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedCategoryResponse.class))
                    )
            }
    )
    public PagedCategoryResponse getCategories(
            @PageableDefault Pageable pageable
    ) {
        PagedDto<ProductCategory> categories = productCategoryService.findAll(pageable);

        return PagedCategoryResponse.from(categories);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "카테고리 등록",
            description = "카테고리를 등록합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "카테고리를 등록합니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "카테고리 등록에 실패했습니다."
                    )
            }
    )
    public void createCategory(@RequestBody CategoryCreateRequest request) {
        productCategoryService.createCategory(request.toDomain());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "카테고리 수정",
            description = "카테고리를 수정합니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "카테고리를 수정합니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "카테고리 수정에 실패했습니다."
                    )
            }
    )
    public void updateCategory(@PathVariable("id") Long id, @RequestBody CategoryUpdateRequest request) {
        productCategoryService.updateCategory(request.toDomainWithId(id));
    }
}
