package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Entity.Product;
import gift.Model.ProductDto;
import gift.Service.CategoryService;
import gift.Service.ProductService;

import java.net.URI;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "상품 관련 api")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    public ResponseEntity<Page<ProductDto>> getAllProductsByRoot(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> paging = productService.getAllProductsByPage(pageable);
        return ResponseEntity.ok(paging);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 세부 정보 조회", description = "상품 세부 정보를 조회합니다.")
    public ResponseEntity<?> getProduct(@PathVariable Long productId) {
        Optional<ProductDto> optionalProduct = productService.getProductById(productId);
        return optionalProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto savedproductDto = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedproductDto);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품에 대한 정보를 수정합니다.")
    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @Valid @RequestBody ProductDto productDtoDetails) {
        ProductDto productDto = productService.updateProduct(productDtoDetails);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        Optional<ProductDto> optionalProduct = productService.getProductById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

}
