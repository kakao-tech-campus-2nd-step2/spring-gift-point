package gift.controller;

import gift.domain.Product.ProductRequest;
import gift.domain.Product.ProductResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
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
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // 상품 추가 데이터 응답
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProductRequest request) {
        Long id = productService.addProduct(request);
        return ResponseEntity.created(URI.create("/api/products" + id)).body(id);
    }

    // 상품 단일 조회 기능
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok().body(response);
    }

    // 상품 삭제 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // 상품 수정 기능
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok().build();
    }
}
