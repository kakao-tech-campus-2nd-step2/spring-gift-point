package gift.controller;

import gift.dto.option.OptionResponse;
import gift.dto.product.ProductCategoryResponse;
import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.entity.Category;
import gift.entity.Option;
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

import java.util.List;
import java.util.stream.Collectors;

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
    @Operation(summary="Get products by CategoryId", description = "Fetches a product by Category ID", tags = {"Product Management System"})
    public ResponseEntity<Page<ProductCategoryResponse>> getProductsByCategoryId(
            @Parameter(description = "ID of the category to be fetched", required = true)
            @RequestParam Long categoryId, Pageable pageable) {
        Page<Product> products = productService.findByCategoryId(categoryId, pageable);
        Page<ProductCategoryResponse> response = products.map(product -> {
            List<OptionResponse> options = product.getOptions().stream()
                    .map(option -> new OptionResponse(option.getId(), option.getName(), option.getQuantity()))
                    .collect(Collectors.toList());
            return new ProductCategoryResponse(
                    product.getId(),
                    product.getCategoryId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    options
            );
        });
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by Id", description = "Fetches a product by its ID", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID of the product to be fetched", required = true)
            @PathVariable Long id) {
        Product product = productService.findById(id);
        ProductResponse response = mapProductToResponse(product);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Add a new product", description = "Adds a new product to the system", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> addProduct(
            @Parameter(description = "Product details", required = true)
            @Valid @RequestBody ProductRequest productRequest) {
        ProductValidator.validateProductRequest(productRequest);

        Category category = productService.getCategoryById(productRequest.getCategoryId());
        Product product = new Product.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .category(category)
                .build();

        List<Option> options = productRequest.getOptions().stream()
                .map(optionRequest -> new Option.Builder()
                        .name(optionRequest.getName())
                        .quantity(optionRequest.getQuantity())
                        .product(product)
                        .build())
                .collect(Collectors.toList());

        Product savedProduct = productService.saveProductWithOptions(product, options);
        ProductResponse response = mapProductToResponse(savedProduct);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates an existing product in the system", tags = { "Product Management System" })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID of the product to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated product details", required = true)
            @Valid @RequestBody ProductRequest productRequest) {

        Product updatedProduct = productService.updateProduct(id, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        ProductResponse response = mapProductToResponse(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product from the system", tags = { "Product Management System" })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted", required = true)
            @PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to map Product to ProductResponse
    private ProductResponse mapProductToResponse(Product product) {
        List<OptionResponse> optionResponses = product.getOptions().stream()
                .map(option -> new OptionResponse(option.getId(), option.getName(), option.getQuantity()))
                .collect(Collectors.toList());

        return new ProductResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                optionResponses
        );
    }
}
