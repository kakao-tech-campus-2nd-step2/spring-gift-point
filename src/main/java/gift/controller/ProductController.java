package gift.controller;

import gift.dto.ApiResponse;
import gift.exception.ForbiddenWordException;
import gift.exception.ProductNotFoundException;
import gift.model.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Product>>> getAllProducts(
        @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) Long categoryId) {
        Page<Product> products = productService.getProducts(pageable, categoryId);
        ApiResponse<Page<Product>> response = new ApiResponse<>(true, "All products retrieved successfully.", products, null);
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
        try {
            productService.createProduct(product);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product created successfully.", product, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ForbiddenWordException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "400");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, "Failed to create product.", null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            productService.updateProduct(id, product);
            Product updatedProduct = productService.getProductById(id);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product updated successfully.", updatedProduct, null);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (ForbiddenWordException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "400");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, "Failed to update product.", null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ApiResponse<Void> response = new ApiResponse<>(true, "Product deleted successfully.", null, null);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException ex) {
            ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), null, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Failed to delete product.", null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            productService.patchProduct(id, updates);
            Product updatedProduct = productService.getProductById(id);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product patched successfully.", updatedProduct, null);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (ForbiddenWordException ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, ex.getMessage(), null, "400");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            ApiResponse<Product> response = new ApiResponse<>(false, "Failed to patch product.", null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
}
