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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<ProductResponseDto>> getPagedProducts(
        @Valid @ModelAttribute PageRequestDto pageRequestDto,
        @RequestParam(value = "categoryId", required = false) Long categoryId) {

        Pageable pageable = pageRequestDto.toPageable();
        Page<ProductResponseDto> productPage;

        if (categoryId != null) {
            productPage = productService.getPagedProductsByCategory(pageable, categoryId);
        } else {
            productPage = productService.getPagedProducts(pageable);
        }

        return ResponseEntity.ok(productPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductRegisterRequestDto productDto) {
        Long newId = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("추가된 상품 id: " + newId);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody ProductRegisterRequestDto productDto) {
        Long updatedId = productService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).body("수정된 상품 id: " + updatedId);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 성공");
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    public ResponseEntity<List<OptionDto>> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptionsByProductId(productId));
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    public ResponseEntity<OptionDto> addOption(@PathVariable("productId") Long productId, @Valid @RequestBody OptionDto optionDto) {
        OptionDto savedOption = productService.saveOption(productId , optionDto);
        return ResponseEntity.ok(savedOption);
    }
}
