package gift.controller;

import gift.dto.pageDTO.PageRequestDTO;
import gift.dto.pageDTO.ProductPageResponseDTO;
import gift.dto.productDTO.ProductAddRequestDTO;
import gift.dto.productDTO.ProductAddResponseDTO;
import gift.dto.productDTO.ProductGetResponseDTO;
import gift.dto.productDTO.ProductUpdateRequestDTO;
import gift.dto.productDTO.ProductUpdateResponseDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 관리 API", description = "상품 관리를 위한 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품을 조회합니다.")
    public ResponseEntity<ProductPageResponseDTO> getAllProduct(@Valid PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size(), Sort.by(pageRequestDTO.sort()));
        ProductPageResponseDTO productPageResponseDTO = productService.findAllProducts(pageable);
        return ResponseEntity.ok(productPageResponseDTO);
    }
    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "ID로 상품을 조회합니다.")
    public ResponseEntity<ProductGetResponseDTO> getProduct(@PathVariable Long productId) {
        ProductGetResponseDTO productGetResponseDTO = productService.findProductById(productId);
        return ResponseEntity.ok(productGetResponseDTO);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public ResponseEntity<ProductAddResponseDTO> addProduct(@RequestBody @Valid ProductAddRequestDTO productAddRequestDTO) {
        ProductAddResponseDTO productAddResponseDTO = productService.saveProduct(productAddRequestDTO);
        return ResponseEntity.status(201).body(productAddResponseDTO);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductUpdateRequestDTO productUpdateRequestDTO) {
        ProductUpdateResponseDTO productUpdateResponseDTO = productService.updateProduct(productUpdateRequestDTO, productId);
        return ResponseEntity.ok(productUpdateResponseDTO);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProductAndWishlistAndOptions(productId);
        return ResponseEntity.noContent().build();
    }
}
