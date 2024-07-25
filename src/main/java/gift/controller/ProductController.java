package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.AddOptionRequest;
import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.MessageResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody UpdateProductRequest product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<Option>> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptions(productId));
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<MessageResponse> addOption(@PathVariable("productId") Long productId, @Valid @RequestBody AddOptionRequest addOptionRequest) {
        return ResponseEntity.ok(productService.addOption(productId, addOptionRequest));
    }

}

