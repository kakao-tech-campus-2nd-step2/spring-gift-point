package gift.product.controller;

import gift.product.dto.OptionDto;
import gift.product.dto.ProductDto;
import gift.product.dto.ProductSortField;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @Operation(summary = "모든 상품 조회")
  public ResponseEntity<Page<ProductDto>> getAllProducts(
      @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
      @RequestParam(defaultValue = "ID") @Parameter(description = "정렬 필드", example = "ID") ProductSortField sort,
      @RequestParam(defaultValue = "ASC") @Parameter(description = "정렬 방향", example = "ASC") Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getFieldName()));
    Page<ProductDto> products = productService.findAll(pageable);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  @Operation(summary = "상품 조회", description = "ID로 특정 상품을 조회")
  public ResponseEntity<ProductDto> getProductById(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id) {
    ProductDto product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  @PostMapping
  @Operation(summary = "상품 생성", description = "새로운 상품을 생성")
  public ResponseEntity<ProductDto> createProduct(
      @Valid @RequestBody @Parameter(description = "상품 데이터", required = true) ProductDto productDto) {
    ProductDto createdProduct = productService.addProduct(productDto);
    return ResponseEntity.ok(createdProduct);
  }

  @PutMapping("/{id}")
  @Operation(summary = "상품 수정", description = "ID로 특정 상품을 수정")
  public ResponseEntity<ProductDto> updateProduct(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id,
      @Valid @RequestBody @Parameter(description = "상품 데이터", required = true) ProductDto productDto) {
    ProductDto updatedProduct = productService.updateProduct(id, productDto);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "상품 삭제", description = "ID로 특정 상품을 삭제")
  public ResponseEntity<?> deleteProduct(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/options")
  @Operation(summary = "상품 옵션 조회", description = "ID로 특정 상품의 옵션을 조회")
  public ResponseEntity<List<OptionDto>> getProductOptions(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id) {
    List<OptionDto> options = productService.getProductOptions(id);
    return ResponseEntity.ok(options);
  }
}
