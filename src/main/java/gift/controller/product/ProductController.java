package gift.controller.product;

import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.controller.order.OrderPageResponse;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.controller.response.PageInfo;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product", description = "Product API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "get All products", description = "상품 조회")
    public ResponseEntity<ApiResponseBody<ProductPageResponse>> getAllProductsByCategoryId(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam UUID categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        var targets = productService.findAllByCategoryId(categoryId, pageable);
        PageInfo pageInfo = new PageInfo(targets.getPageable().getPageNumber(),
            targets.getTotalElements(), targets.getTotalPages());
        return new ApiResponseBuilder<ProductPageResponse>()
            .httpStatus(HttpStatus.OK)
            .data(new ProductPageResponse(pageInfo, targets.toList()))
            .messages("모든 상품 조회")
            .build();
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get product", description = "상품 조회")
    public ResponseEntity<ApiResponseBody<ProductResponse>> getProduct(
        @PathVariable UUID productId) {
        return new ApiResponseBuilder<ProductResponse>()
            .httpStatus(HttpStatus.OK)
            .data(productService.getProductResponse(productId))
            .messages("상품 조회")
            .build();
    }

    @PostMapping
    @Operation(summary = "create product", description = "상품 생성")
    public ResponseEntity<ApiResponseBody<ProductResponse>> createProduct(
        @LoginUser LoginResponse loginMember,
        @RequestBody ProductRequest product) {
        return new ApiResponseBuilder<ProductResponse>()
            .httpStatus(HttpStatus.OK)
            .data(productService.save(product))
            .messages("상품 생성")
            .build();
    }

    @PutMapping("/{productId}")
    @Operation(summary = "modify product", description = "상품 수정")
    public ResponseEntity<ApiResponseBody<ProductResponse>> updateProduct(
        @LoginUser LoginResponse loginMember,
        @PathVariable UUID productId, @RequestBody ProductRequest product) {
        return new ApiResponseBuilder<ProductResponse>()
            .httpStatus(HttpStatus.OK)
            .data(productService.update(productId, product))
            .messages("상품 수정")
            .build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "delete product", description = "상품 삭제")
    public ResponseEntity<ApiResponseBody<Void>> deleteProduct(@LoginUser LoginResponse loginMember,
        @PathVariable UUID productId) {
        productService.delete(productId);
        return new ApiResponseBuilder<Void>()
            .httpStatus(HttpStatus.OK)
            .data(null)
            .messages("상품 삭제")
            .build();
    }
}