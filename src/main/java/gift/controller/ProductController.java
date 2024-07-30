package gift.controller;

import gift.dto.ProductDTO;
import gift.exception.NoOptionsForProductException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable) {
        return ResponseEntity.ok().body(productService.getProducts(pageable));
    }

    @Operation(summary = "한 상품 조회", description = "해당 id의 상품을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        if(productDTO.optionDTOs() == null || productDTO.optionDTOs().size() == 0) {
            throw new NoOptionsForProductException();
        }
        ProductDTO addedProductDTO = productService.addProduct(productDTO);
        return ResponseEntity.ok().body(addedProductDTO);
    }

    @Operation(summary = "상품 수정", description = "해당 id의 상품을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") long id, @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok().body(productService.updateProduct(id, productDTO));
    }

    @Operation(summary = "상품 삭제", description = "해당 id의 상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }
}
