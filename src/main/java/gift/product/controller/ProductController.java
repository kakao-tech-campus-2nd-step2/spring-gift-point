package gift.product.controller;

import gift.product.dto.*;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  @Operation(summary = "모든 상품 조회", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
  public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
      @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
      @RequestParam(defaultValue = "CREATED_AT") @Parameter(description = "정렬 필드", example = "ID") ProductSortField sort,
      @RequestParam(defaultValue = "DESC") @Parameter(description = "정렬 방향", example = "ASC") Sort.Direction direction,
      @RequestParam @Parameter(description = "카테고리 ID", example = "1") Long categoryId) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getFieldName()));
    Page<ProductResponseDto> products = productService.getAllProducts(categoryId, pageable);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
  public ResponseEntity<ProductResponseDto> getProductById(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id) {
    ProductResponseDto productResponseDto = productService.getProductById(id);
    return ResponseEntity.ok(productResponseDto);
  }

  @PostMapping
  @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
  public ResponseEntity<ProductResponseDto> createProduct(
      @Valid @RequestBody @Parameter(description = "상품 데이터", required = true) ProductRequestDto productRequestDto,
      @RequestBody @Parameter(description = "옵션 데이터", required = false) OptionRequestDto optionRequestDto) {
    ProductResponseDto productResponseDto = productService.createProduct(productRequestDto,
        optionRequestDto);
    return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(summary = "상품 수정", description = "ID로 특정 상품을 수정")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @PathVariable @Parameter(description = "상품 ID", required = true) long id,
      @Valid @RequestBody @Parameter(description = "상품 데이터", required = true) ProductRequestDto productRequestDto) {
    ProductResponseDto productResponseDto = productService.updateProduct(id, productRequestDto);
    return ResponseEntity.ok(productResponseDto);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
  public ResponseEntity<String> deleteProduct(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId) {
    productService.deleteProduct(productId);
    return new ResponseEntity<>("상품이 성공적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
  }

}
