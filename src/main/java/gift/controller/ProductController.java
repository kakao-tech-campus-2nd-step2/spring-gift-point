package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.AddOptionRequest;
import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.MessageResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "상품과 관련된 API Controller")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 목록 조회 api")
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "특정 제품 정보 조회 api")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping
    @Operation(summary = "새 상품 추가 api")
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 정보 수정 api")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody UpdateProductRequest product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제 api")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "특정 상품의 모든 옵션 조회 api")
    public ResponseEntity<List<Option>> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptions(productId));
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "특정 상품의 옵션 추가 api")
    public ResponseEntity<MessageResponse> addOption(@PathVariable("productId") Long productId, @Valid @RequestBody AddOptionRequest addOptionRequest) {
        return ResponseEntity.ok(productService.addOption(productId, addOptionRequest));
    }

}

