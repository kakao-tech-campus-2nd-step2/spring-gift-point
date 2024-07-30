package gift.controller;

import gift.constants.SuccessMessage;
import gift.dto.ProductRequest;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok(SuccessMessage.ADD_PRODUCT_SUCCESS_MSG);
    }

    @PutMapping()
    public ResponseEntity<String> editProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.editProduct(productRequest);
        return ResponseEntity.ok(SuccessMessage.EDIT_PRODUCT_SUCCESS_MSG);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(SuccessMessage.DELETE_PRODUCT_SUCCESS_MSG);
    }
}
