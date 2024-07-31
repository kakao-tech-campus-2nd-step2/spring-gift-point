package gift.controller;

import gift.dto.ProductDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "ProductController", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "모든 상품 조회", description = "모든 상품을 페이지네이션하여 조회합니다.")
  @GetMapping
  public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
    return ResponseEntity.ok(productService.findAll(pageable));
  }

  @Operation(summary = "상품 ID로 조회", description = "상품 ID로 특정 상품을 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
    return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    ProductDto createdProduct = productService.createProduct(productDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @Operation(summary = "상품 수정", description = "상품 ID로 특정 상품을 수정합니다.")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto product) {
    if (productService.updateProduct(id, product)) {
      return ResponseEntity.ok(product);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "상품 삭제", description = "상품 ID로 특정 상품을 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    if (!productService.findById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    productService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}