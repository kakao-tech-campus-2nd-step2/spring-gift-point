package gift.controller;

import gift.dto.ProductDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "상품 API")
@RestController
@RequestMapping(value = "/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    private ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 추가(Create)
    @Operation(summary = "상품 생성", description = "새 상품을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.createProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 단일 상품 조회(Read)
    @Operation(summary = "단일 상품 조회", description = "특정 상품을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> selectProduct(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.getProduct(id);
        return productDTO
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 전체 상품 조회(Read)
    @Operation(summary = "전체 상품 조회", description = "전체 상품 정보를 가져옵니다.")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> selectAllProducts(
        @Min(value = 1, message = "페이지 번호는 1이상이어야 합니다.")
        @RequestParam(value = "page", defaultValue = "0") int page,

        @Min(value = 1, message = "한 페이지당 1개 이상의 항목이 포함되어야 합니다.")
        @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 상품 수정(Update)
    @Operation(summary = "상품 수정", description = "지정된 상품의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productService.editProduct(id, productDTO);
        return ResponseEntity.ok("상품이 수정 되었습니다.");
    }

    // 상품 삭제(Delete)
    @Operation(summary = "상품 삭제", description = "지정된 상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("상품이 삭제 되었습니다.");
    }
}