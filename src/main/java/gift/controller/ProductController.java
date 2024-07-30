package gift.controller;

import gift.dto.request.ProductRequestDTO;
import gift.dto.response.ProductResponseDTO;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("productId") Long productId) {
        ProductResponseDTO productResponseDTO = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productResponseDTO);
    }

    @PostMapping("")
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Product added successfully");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.removeProduct(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted successfully");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long productId, @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.updateProduct(productId, productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product updated successfully");
    }



}

