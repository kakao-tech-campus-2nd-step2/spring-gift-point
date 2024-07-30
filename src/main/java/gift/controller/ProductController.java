package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.CreateProductDto;
import gift.dto.UpdateProductDto;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    // 상품 추가
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return ResponseEntity.ok(product);
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> allProducts = productService.getAllProducts(pageable);
        if (allProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(allProducts);
    }

    // 특정 상품 조회
    @GetMapping("/{product_id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long product_id) {
        Product product = productService.getProduct(product_id);
        return ResponseEntity.ok(product);
    }

    // 상품 정보 update
    @PutMapping("/{product_id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long product_id, @Valid @RequestBody UpdateProductDto productDto) {
        Product updatedProduct = productService.updateProduct(product_id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 정보 삭제
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long product_id) {
        productService.deleteProduct(product_id);
        return ResponseEntity.ok().build();
    }

    // 특정 상품의 옵션 가져오기
    @GetMapping("/{product_id}/options")
    public ResponseEntity<List<Option>> getProductOptions(@PathVariable Long product_id) {
        List<Option> options = optionService.getProductOptions(product_id);
        return ResponseEntity.ok(options);
    }
}
