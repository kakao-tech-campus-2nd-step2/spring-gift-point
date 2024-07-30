package gift.main.controller;

import gift.main.dto.*;
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

    @GetMapping()
    public ResponseEntity<?> getProductPage(@RequestParam(value = "page") int pageNum) {
        Page<ProductResponse> productPage = productService.getProductPage(pageNum);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findProduct(@PathVariable(name = "id") Long id) {
        ProductResponse product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

//    @GetMapping("/product/{id}/options")
//    public ResponseEntity<?> findAllOption(@PathVariable(value = "id") long productId) {
//        ProductResponse product = productService.getProduct(productId);
//        List<OptionResponse> options = optionService.findAllOption(productId);
//        ProductAllResponse productAllResponse = new ProductAllResponse(product);
//        return ResponseEntity.ok(productAllResponse);
//    }

    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductAllRequest productAllRequest) {
        productService.registerProduct(productAllRequest);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") long id,
            @Valid @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok("Product updated successfully");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
