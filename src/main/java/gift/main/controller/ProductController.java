package gift.main.controller;

import gift.main.dto.ProductAllRequest;
import gift.main.dto.ProductRequest;
import gift.main.dto.ProductResponse;
import gift.main.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }

    //카테고리 별 제품 페이지 조회
    @GetMapping()
    public ResponseEntity<?> getProductPage(@RequestParam(value = "page") int pageNum,
                                            @RequestParam(value = "category") int categoryId) {
        Page<ProductResponse> productPage = productService.getProductPage(pageNum,categoryId);
        return ResponseEntity.ok(productPage);
    }

    //특정 제품 조회
    @GetMapping("{id}")
    public ResponseEntity<?> findProduct(@PathVariable(name = "id") Long id) {
        ProductResponse product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    //새로운 제품 추가
    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductAllRequest productAllRequest) {
        productService.registerProduct(productAllRequest);
        return ResponseEntity.ok("Product added successfully");
    }

    //제품 정보 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") long id,
            @Valid @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok("Product updated successfully");

    }

    //제품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
