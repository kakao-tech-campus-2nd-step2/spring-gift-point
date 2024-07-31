package gift.controller;

import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@Tag(name = "Product API", description = "상품 관리 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 페이징하여 조회합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
        @Parameter(description = "카테고리 ID", required = false) @RequestParam(required = false) Long categoryId
    ) {
        Page<ProductResponse> products = productService.getAllProducts(pageable, categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품 조회", description = "ID를 사용하여 특정 상품을 조회합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 상품 Id",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + PRODUCT_NOT_FOUND + "(상품 Id)\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
        @Valid @RequestBody ProductCreateRequest productCreateRequest
    ) {
        ProductResponse createdProduct = productService.addProduct(productCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequest productUpdateRequest
    ) {
        ProductResponse updatedProduct = productService.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "상품 삭제", description = "기존 상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
