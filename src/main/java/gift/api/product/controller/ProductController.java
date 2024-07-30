package gift.api.product.controller;

import gift.api.product.dto.ProductRequest;
import gift.api.product.dto.ProductResponse;
import gift.api.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

    @GetMapping()
    @Operation(summary = "상품 조회", description = "전체 상품 페이지별 조회")
    public ResponseEntity<List<ProductResponse>> getProducts(Pageable pageable) {
        return ResponseEntity.ok().body(productService.getProducts(pageable));
    }

    @PostMapping()
    @Operation(summary = "상품 추가")
    public ResponseEntity<Void> add(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.created(
            URI.create("/api/products/" + productService.add(productRequest))).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest productRequest) {
        productService.update(id, productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
