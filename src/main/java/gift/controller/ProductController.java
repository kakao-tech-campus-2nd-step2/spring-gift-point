package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
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
                product.getImageUrl()
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body(errors);
        }
        Product product = new Product(
                null,
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                null // Category 설정
        );
        return ResponseEntity.ok(new ProductResponse(
                productService.save(product).getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
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
        Product existingProduct = existingOpt.get();
        existingProduct.update(
          productRequest.getPrice(),
          productRequest.getName(),
          productRequest.getImageUrl(),
          null // Category
        );
        return ResponseEntity.ok(new ProductResponse(
                productService.save(existingProduct).getId(),
                existingProduct.getName(),
                existingProduct.getPrice(),
                existingProduct.getImageUrl()
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
