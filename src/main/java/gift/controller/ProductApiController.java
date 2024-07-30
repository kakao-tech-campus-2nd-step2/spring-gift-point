package gift.controller;

import gift.dto.ProductDto;
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
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Page<Product>> getAllProducts(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productService.getProducts(pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 id로 상품 조회", description = "상품 id로 상품을 조회합니다.")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid ProductDto productDto) {
        Product savedProduct = productService.addProduct(productDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 id로 상품을 수정합니다.")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody @Valid ProductDto productDto) {
        Product product = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품 id로 상품을 삭제합니다.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
