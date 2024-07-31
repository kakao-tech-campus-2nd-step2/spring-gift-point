package gift.controller;

import gift.dto.ProductDetailDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.model.Product;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "Product", description = "상품 관련 api")
@RequestMapping("/api/products")
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponseDto> productPage = productService.getApiProducts(pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 id로 상품 조회", description = "상품 id로 상품을 조회합니다.")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable Long productId) {
        ProductDetailDto product = productService.getApiProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        ProductResponseDto savedProduct = productService.addProduct(productRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 id로 상품을 수정합니다.")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody @Valid ProductRequestDto productRequestDto) {
        Product product = productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품 id로 상품을 삭제합니다.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
