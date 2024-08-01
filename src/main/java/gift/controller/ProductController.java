package gift.controller;

import gift.dto.ApiResponse;
import gift.exception.ProductNotFoundException;
import gift.model.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ApiResponse<List<Product>> response = new ApiResponse<>(true, "All products retrieved successfully.", products, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product retrieved successfully.", product, null);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        boolean success = productService.createProduct(product);
        if (success) {
            ApiResponse<Product> response = new ApiResponse<>(true, "Product created successfully.", product, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        ApiResponse<Product> response = new ApiResponse<>(false, "Failed to create product.", null, "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        boolean success = productService.updateProduct(id, product);
        if (success) {
            Product updatedProduct = productService.getProductById(id);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product updated successfully.", updatedProduct, null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<Product> response = new ApiResponse<>(false, "Failed to update product.", null, "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        boolean success = productService.patchProduct(id, updates);
        if (success) {
            Product updatedProduct = productService.getProductById(id);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product patched successfully.", updatedProduct, null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<Product> response = new ApiResponse<>(false, "Failed to patch product.", null, "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<List<Product>>> patchProducts(@RequestBody List<Map<String, Object>> updatesList) {
        List<Product> updatedProducts = productService.patchProducts(updatesList);
        int originalCount = updatesList.size();
        int updateCount = updatedProducts.size();
        ApiResponse<List<Product>> response;

        if (updateCount == originalCount) {
            response = new ApiResponse<>(true, "All products patched successfully.", updatedProducts, null);
            return ResponseEntity.ok(response);
        }

        if (updateCount > 0) {
            response = new ApiResponse<>(true, "Some products patched successfully.", updatedProducts, null);
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }

        response = new ApiResponse<>(false, "No products patched.", null, "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        if (success) {
            ApiResponse<Void> response = new ApiResponse<>(true, "Product deleted successfully.", null, null);
            return ResponseEntity.noContent().build();
        }
        ApiResponse<Void> response = new ApiResponse<>(false, "Failed to delete product.", null, "500");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
