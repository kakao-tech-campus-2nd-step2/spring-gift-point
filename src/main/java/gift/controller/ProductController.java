package gift.controller;

import gift.entity.Product;
import gift.domain.ProductDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.net.URI;
import java.util.Optional;

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.getAllProduct(page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(productPage.getNumber()));
        headers.add("X-Page-Size", String.valueOf(productPage.getSize()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(productPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<URI> addProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        URI uri = new URI("/api/products/" + productService.addProduct(productDTO));
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }
}