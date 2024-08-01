package gift.controller;

import gift.constants.SuccessMessage;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable("productId") Long productId) {
        return productService.getProduct(productId);
    }

    @GetMapping
    public PagedModel<ProductResponse> getAllProducts(@PageableDefault(size = 5) Pageable pageable,
        @RequestParam("categoryId") Long categoryId) {
        return new PagedModel<>(productService.getAllProducts(categoryId, pageable));
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok(SuccessMessage.ADD_PRODUCT_SUCCESS_MSG);
    }

    @PutMapping
    public ResponseEntity<String> editProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.editProduct(productRequest);
        return ResponseEntity.ok(SuccessMessage.EDIT_PRODUCT_SUCCESS_MSG);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(SuccessMessage.DELETE_PRODUCT_SUCCESS_MSG);
    }
}
