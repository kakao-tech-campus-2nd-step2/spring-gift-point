package gift.product.controller;

import gift.option.dto.OptionResDto;
import gift.product.dto.ProductReqDto;
import gift.product.dto.ProductResDto;
import gift.product.message.ProductInfo;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
@Tag(name = "상품 API", description = "상품 관리 API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공"),
            })
    public ResponseEntity<Page<ProductResDto>> getProducts(Pageable pageable) {
        Page<ProductResDto> productResDtos = productService.getProducts(pageable);

        return ResponseEntity.ok(productResDtos);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "상품을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
            })
    public ResponseEntity<ProductResDto> getProduct(@PathVariable("productId") Long productId) {
        ProductResDto productResDto = productService.getProduct(productId);

        return ResponseEntity.ok(productResDto);
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회", description = "상품의 모든 옵션 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 옵션 목록 조회 성공"),
            })
    public ResponseEntity<List<OptionResDto>> getProductOptions(@PathVariable("productId") Long productId) {
        List<OptionResDto> optionResDtos = productService.getProductOptions(productId);

        return ResponseEntity.ok(optionResDtos);
    }

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품을 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
            })
    public ResponseEntity<ProductResDto> addProduct(@Valid @RequestBody ProductReqDto productReqDto) {
        ProductResDto productResDto = productService.addProduct(productReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResDto);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            })
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody ProductReqDto productReqDto) {
        productService.updateProduct(productId, productReqDto);

        return ResponseEntity.ok(ProductInfo.PRODUCT_UPDATE_SUCCESS);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            })
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.ok(ProductInfo.PRODUCT_DELETE_SUCCESS);
    }
}
