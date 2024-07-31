package gift.controller;

import gift.dto.OptionDto;
import gift.dto.PageRequestDto;
import gift.dto.ProductRegisterRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
@Tag(name = "Product", description = "상품 API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회합니다.")
    public ResponseEntity<Page<ProductResponseDto>> getPagedProducts(@Valid PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();
        Page<ProductResponseDto> productPage = productService.getPagedProducts(pageable);
        return ResponseEntity.ok(productPage);
    }

    @PostMapping("/add")
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRegisterRequestDto productDto) {
        Long newId = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("추가된 상품 id: " + newId);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public ResponseEntity<String> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductRegisterRequestDto productDto) {
        Long updatedId = productService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body("수정된 상품 id: " + updatedId);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 성공");
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 조회", description = "상품의 옵션을 조회합니다.")
    public ResponseEntity<List<OptionDto>> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptionsByProductId(productId));
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "상품의 옵션을 추가합니다.")
    public ResponseEntity<OptionDto> addOption(@PathVariable("productId") Long productId, @Valid @RequestBody OptionDto optionDto) {
        OptionDto savedOption = productService.saveOption(productId , optionDto);
        return ResponseEntity.ok(savedOption);
    }
}
