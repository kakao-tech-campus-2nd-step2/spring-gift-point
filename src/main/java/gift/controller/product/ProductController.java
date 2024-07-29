package gift.controller.product;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Product", description = "Product API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "get All products", description = "상품 조회")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(pageable));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get product", description = "상품 조회")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductResponse(productId));
    }

    @PostMapping
    @Operation(summary = "create product", description = "상품 생성")
    public ResponseEntity<ProductResponse> createProduct(@LoginUser LoginResponse loginMember,
        @RequestBody ProductRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "modify product", description = "상품 수정")
    public ResponseEntity<ProductResponse> updateProduct(@LoginAdmin LoginResponse loginMember,
        @PathVariable UUID productId, @RequestBody ProductRequest product) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, product));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "delete product", description = "상품 삭제")
    public ResponseEntity<Void> deleteProduct(@LoginAdmin LoginResponse loginMember,
        @PathVariable UUID productId) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
