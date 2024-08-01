package gift.api.product.controller;

import gift.api.product.dto.ProductRequest;
import gift.api.product.dto.ProductResponse;
import gift.api.product.service.ProductService;
import gift.global.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 상품 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 상품 ID"),
    })
    public ResponseEntity<ProductResponse> getProduct(
        @Parameter(required = true, description = "조회할 상품의 ID")
        @PathVariable("id") Long id) {

        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping
    @Operation(summary = "상품 조회", description = "전체 상품 페이지별 조회")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
        @Parameter(description = "페이지네이션 요청")
        Pageable pageable) {

        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @PostMapping
    @Operation(summary = "상품 추가")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
    })
    public ResponseEntity<Void> add(
        @Parameter(required = true, description = "상품 요청 본문")
        @RequestBody @Valid ProductRequest productRequest) {

        return ResponseEntity.created(
            URI.create("/api/products/" + productService.add(productRequest))).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
    })
    public ResponseEntity<Void> update(
        @Parameter(required = true, description = "수정할 상품의 ID")
        @PathVariable("id") Long id,
        @Parameter(required = true, description = "상품 요청 본문")
        @Valid @RequestBody ProductRequest productRequest) {

        productService.update(id, productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Void> delete(
        @Parameter(required = true, description = "삭제할 상품의 ID")
        @PathVariable("id") Long id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
