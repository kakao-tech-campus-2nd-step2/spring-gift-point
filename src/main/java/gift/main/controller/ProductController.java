package gift.main.controller;

import gift.main.dto.OptionResponse;
import gift.main.dto.ProductAllResponse;
import gift.main.dto.ProductResponce;
import gift.main.service.OptionService;
import gift.main.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;

    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductPage(Pageable pageable) {
        Page<ProductResponce> productPage = productService.getProductPage(pageable);
        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> findProduct(@PathVariable(name = "id") Long id) {
        ProductResponce product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/product/{id}/options")
    public ResponseEntity<?> findAllOption(@PathVariable(value = "id") long productId) {
        ProductResponce product = productService.getProduct(productId);
        List<OptionResponse> options = optionService.findAllOption(productId);
        ProductAllResponse productAllResponse = new ProductAllResponse(product, options);
        return ResponseEntity.ok(productAllResponse);
    }
}
