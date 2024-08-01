package gift.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ProductResponse addProduct(@Valid @RequestBody ProductOptionRequest productOptionRequest) {
        return new ProductResponse(productService.insertNewProduct(productOptionRequest));
    }

    @PutMapping("/products/{id}")
    @Transactional
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product) {

        return new ProductResponse(productService.updateProduct(id, product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductReadResponse>> getProductsPage(@RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                                                 @RequestParam(required = false, defaultValue = "10", value = "size") @Min(1) @Max(20) int size,
                                                                 @RequestParam(required = false, defaultValue = "id", value = "sort") List<String> sort,
                                                                 @RequestParam(defaultValue = "0", value = "categoryId") Long categoryId) {

        return ResponseEntity.ok(productService.getProductPages(page, size, sort, categoryId));

    }

    @GetMapping("/products/{id}")
    public ProductReadResponse getProduct(@PathVariable long id) {
        return new ProductReadResponse(productService.findByID(id));

    }
}
