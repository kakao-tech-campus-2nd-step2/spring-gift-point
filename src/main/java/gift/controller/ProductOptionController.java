package gift.controller;

import gift.dto.ProductOptionRequestDto;
import gift.dto.ProductOptionResponseDto;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductOptions", description = "상품 옵션 관련 API")
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "새로운 상품 옵션을 추가합니다.")
    public ResponseEntity<ProductOptionResponseDto> addProductOption(@PathVariable Long productId, @RequestBody ProductOptionRequestDto productOptionRequestDto) {
        ProductOptionResponseDto createdProductOption = productOptionService.addProductOption(productOptionRequestDto);
        return new ResponseEntity<>(createdProductOption, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품의 모든 옵션 조회", description = "특정 상품의 모든 옵션을 조회합니다.")
    public ResponseEntity<List<ProductOptionResponseDto>> getProductOptions(@PathVariable Long productId) {
        List<ProductOptionResponseDto> productOptions = productOptionService.getProductOptions(productId);
        return new ResponseEntity<>(productOptions, HttpStatus.OK);
    }

    @PutMapping("/options/{id}")
    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션을 수정합니다.")
    public ResponseEntity<ProductOptionResponseDto> updateProductOption(@PathVariable Long id, @RequestBody ProductOptionRequestDto productOptionRequestDto) {
        ProductOptionResponseDto updatedProductOption = productOptionService.updateProductOption(id, productOptionRequestDto);
        return new ResponseEntity<>(updatedProductOption, HttpStatus.OK);
    }

    @DeleteMapping("/options/{id}")
    @Operation(summary = "상품 옵션 삭제", description = "기존 상품 옵션을 삭제합니다.")
    public ResponseEntity<Void> deleteProductOption(@PathVariable Long id) {
        productOptionService.deleteProductOption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
