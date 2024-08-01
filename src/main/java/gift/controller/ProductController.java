package gift.controller;

import gift.dto.ProductDto;
import gift.dto.ProductPageResponse;
import gift.exception.CustomNotFoundException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

@Tag(name = "ProductController", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
  @PostMapping
  public ResponseEntity<Page<ProductDto>> createProduct(@Valid @RequestBody ProductDto productDto, Pageable pageable) {
    Page<ProductDto> createdProductPage = productService.createProduct(productDto, pageable);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProductPage);
  }

  @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
  @GetMapping("/{id}")
  public ResponseEntity<Page<ProductDto>> getProductById(@PathVariable Long id, Pageable pageable) {
    return productService.findById(id)
            .map(productDto -> {
              List<ProductDto> productDtos = Collections.singletonList(productDto);
              Page<ProductDto> productPage = new PageImpl<>(productDtos, pageable, 1);
              return ResponseEntity.ok(productPage);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
    try {
      ProductDto updatedProduct = productService.updateProduct(id, productDto);
      return ResponseEntity.ok(updatedProduct);
    } catch (CustomNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    if (!productService.findById(id).isPresent()) {
      return ResponseEntity.notFound().build();
    }
    productService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
  @GetMapping
  public ResponseEntity<ProductPageResponse> getAllProducts(Pageable pageable) {
    Page<ProductDto> productPage = productService.findAll(pageable);
    ProductPageResponse response = new ProductPageResponse(
            productPage.getContent(),
            productPage.getNumber(),
            productPage.getTotalPages(),
            productPage.getTotalElements()
    );
    return ResponseEntity.ok(response);
  }
}