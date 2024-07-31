package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.ProductService;
import gift.util.ProductValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management System", description = "Operations related to product management")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Fetches all available products", tags = { "Product Management System" })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        Page<ProductResponse> response = products.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getName()
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by Id", description = "Fetches a product by its ID", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID of the product to be fetched", required = true)
            @PathVariable Long id) {
        Product product = productService.findById(id);
        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getName()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Add a new product", description = "Adds a new product to the system", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> addProduct(
            @Parameter(description = "Product details", required = true)
            @Valid @RequestBody ProductRequest productRequest) {
        ProductValidator.validateProductRequest(productRequest);

        Category category = productService.getCategoryById(productRequest.getCategoryId());
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                category);

        Product savedProduct = productService.save(product);

        return ResponseEntity.status(201).body(new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getImageUrl(),
                savedProduct.getCategory().getName()
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates an existing product in the system", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID of the product to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product details", required = true)
            @Valid @RequestBody ProductRequest productRequest) {
        ProductValidator.validateProductRequest(productRequest);
        Product existingProduct = productService.findById(id);

        Category category = productService.getCategoryById(productRequest.getCategoryId());
        existingProduct.update(
                productRequest.getPrice(),
                productRequest.getName(),
                productRequest.getImageUrl(),
                category
        );
        Product updatedProduct = productService.save(existingProduct);
        return ResponseEntity.ok(new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getImageUrl(),
                updatedProduct.getCategory().getName()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product from the system", tags = { "Product Management System" })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted", required = true)
            @PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
