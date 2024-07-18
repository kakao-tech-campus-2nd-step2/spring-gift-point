package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(Pageable pageable) {
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

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body(errors);
        }
        Category category = productService.getCategoryById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                category
        );
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(new ProductResponse(
           savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getImageUrl(),
                savedProduct.getCategory().getName()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<Product> existingOpt = productService.findById(id);
        if(existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category category = productService.getCategoryById(productRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Product existingProduct = existingOpt.get();
        existingProduct.update(
                productRequest.getPrice(),
                productRequest.getName(),
                productRequest.getImageUrl(),
                category
        );
        Product updatedProduct = productService.save(existingProduct);
        return ResponseEntity.ok(new ProductResponse( //Long id, String name, Integer price, String imageUrl, String categoryName
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getImageUrl(),
                updatedProduct.getCategory().getName()
        ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if(productService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
