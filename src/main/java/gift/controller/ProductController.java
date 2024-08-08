package gift.controller;

import gift.domain.option.OptionRequest;
import gift.domain.product.Product;
import gift.domain.product.ProductRequest;
import gift.domain.product.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public List<Product> readProducts(
        @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
        @RequestParam(required = false, defaultValue = "10", value = "size") int pageSize) {
        return productService.findAll(pageNo, pageSize);
    }

    @GetMapping("/{productId}")
    public Product readProduct(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = new ProductResponse(productService.save(productRequest));
        List<OptionRequest> options = productRequest.options();
        options.forEach(optionRequest -> optionService.save(productResponse.id(), optionRequest));
        return ResponseEntity.created(URI.create("/api/products/" + productResponse.id()))
            .body(productResponse);
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId,
        @Valid @RequestBody ProductRequest productRequest) {
        return new ProductResponse(productService.update(productId, productRequest));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
