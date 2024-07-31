package gift.product.restapi;

import gift.advice.ErrorResponse;
import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductService;
import gift.core.domain.product.exception.ProductNotFoundException;
import gift.product.restapi.dto.request.ProductCreateRequest;
import gift.product.restapi.dto.request.ProductUpdateRequest;
import gift.product.restapi.dto.response.PagedProductResponse;
import gift.product.restapi.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "상품")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products")
    @Operation(
            summary = "상품 목록 조회",
            description = "상품 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (기본 값 : 0)"),
                    @Parameter(name = "size", description = "페이지 크기 (기본 값 : 10)")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "상품 목록을 조회합니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedProductResponse.class))
    )
    public PagedProductResponse getAllProducts(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PagedDto<Product> pagedProducts = productService.findAll(pageable);
        return PagedProductResponse.from(pagedProducts);
    }

    @GetMapping("/api/products/{id}")
    @Operation(
            summary = "상품 조회",
            description = "상품을 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "상품 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품을 조회합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ProductResponse getProduct(@PathVariable Long id) {
        return ProductResponse.from(productService.get(id));
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "상품을 등록합니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품 등록 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public void addProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        productService.createProductWithCategory(request.categoryName(), request.toDomain());
    }

    @PutMapping("/api/products/{id}")
    @Operation(
            summary = "상품 수정",
            description = "상품을 수정합니다.",
            parameters = {
                    @Parameter(name = "id", description = "상품 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품을 수정합니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "상품 수정 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public void updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        Product originalProduct = productService.get(id);
        if (originalProduct == null) {
            throw new ProductNotFoundException();
        }
        Product updatedProduct = originalProduct.applyUpdate(
                request.name(),
                request.price(),
                request.imageUrl()
        );
        productService.updateProduct(request.categoryName(), updatedProduct);
    }

    @DeleteMapping("/api/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "상품 삭제",
            description = "상품을 삭제합니다.",
            parameters = {
                    @Parameter(name = "id", description = "상품 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "상품을 삭제합니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품을 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public void deleteProduct(@PathVariable Long id) {
        productService.remove(id);
    }
}
