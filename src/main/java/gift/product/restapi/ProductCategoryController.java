package gift.product.restapi;

import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductCategoryService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                            content = @Content(mediaType = "application/json", schema = @Schema(contentSchema = PagedCategoryResponse.class))
                    )
            }
    )
    public PagedCategoryResponse getCategories(
            @PageableDefault Pageable pageable
    ) {
        PagedDto<ProductCategory> categories = productCategoryService.findAll(pageable);

        return PagedCategoryResponse.from(categories);
    }
}
