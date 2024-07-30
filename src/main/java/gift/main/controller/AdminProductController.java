package gift.main.controller;

import gift.main.annotation.SessionUser;
import gift.main.dto.ProductAllRequest;
import gift.main.dto.ProductRequest;
import gift.main.dto.UserVo;
import gift.main.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> registerProduct(@RequestBody ProductAllRequest productAllRequest,
                                                  @SessionUser UserVo sessionUserVo) {
        productService.registerProduct(productAllRequest, sessionUserVo);
        System.out.println("productAllInformation = " + productAllRequest);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") long id,
            @Valid @RequestBody ProductRequest productRequest,
            @SessionUser UserVo sessionUserVo) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok("Product updated successfully");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

}
