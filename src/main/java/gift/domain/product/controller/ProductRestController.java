package gift.domain.product.controller;

import gift.domain.product.dto.ProductResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductReadAllResponse;
import gift.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "상품 API")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    public ResponseEntity<ProductResponse> create(
        @Parameter(description = "상품 요청 정보", required = true)
        @RequestBody @Valid ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.create(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping
    @Operation(summary = "상품 전체 조회", description = "상품 목록을 조회합니다.")
    public ResponseEntity<Page<ProductReadAllResponse>> readAll(
        @Parameter(description = "페이징 정보", in = ParameterIn.QUERY)
        Pageable pageable
    ) {
        Page<ProductReadAllResponse> productResponses = productService.readAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(productResponses);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 상세 조회", description = "옵션을 포함한 특정 상품 정보를 조회합니다.")
    public ResponseEntity<ProductResponse> readById(
        @Parameter(description = "상품 ID", in = ParameterIn.PATH, required = true)
        @PathVariable("productId") long productId
    ) {
        ProductResponse productResponse = productService.readById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    public ResponseEntity<ProductResponse> update(
        @Parameter(description = "상품 ID", in = ParameterIn.PATH, required = true)
        @PathVariable("productId") long productId,
        @Parameter(description = "상품 요청 정보", required = true)
        @RequestBody @Valid ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.update(productId, productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<Void> delete(
        @Parameter(description = "상품 ID", in = ParameterIn.PATH, required = true)
        @PathVariable("productId") long productId
    ) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
