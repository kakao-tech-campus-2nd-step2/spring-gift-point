package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity makeProduct(@RequestBody @Valid ProductRequest request) {
        productService.makeProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<ProductResponse> productsList = productService.getAllProducts(pageable);
        return ResponseEntity.ok().body(productsList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/products")
    public ResponseEntity putProduct(@RequestBody @Valid ProductRequest request) {
        productService.putProduct(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}