package gift.controller;

import gift.dto.ProductCreateRequestDTO;
import gift.dto.ProductCreateResponseDTO;
import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.enums.SortDirection;
import gift.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 모든 상품 조회
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "ASC") SortDirection direction,
        @RequestParam Long categoryId) {

        Sort sort = Sort.by(direction == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<ProductResponseDTO> products = productService.getAllProducts(categoryId, pageRequest);
        return ResponseEntity.ok(products);
    }

    // 특정 ID의 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 새로운 상품 생성
    @PostMapping
    public ResponseEntity<ProductCreateResponseDTO> createProduct(
        @RequestBody ProductCreateRequestDTO productRequestDTO,
        @RequestHeader("Authorization") String token) {
        ProductCreateResponseDTO createdProduct = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 기존 상품 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequest) {
        return productService.updateProduct(id, productRequest)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 특정 ID 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
