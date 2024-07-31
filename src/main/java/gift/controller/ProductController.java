package gift.controller;

import gift.dto.request.AddOptionRequest;
import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.MessageResponse;
import gift.dto.response.ProductOptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "상품과 관련된 API Controller")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*Product api*/
    @GetMapping
    @Operation(summary = "모든 상품 목록 조회 api")
    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        return new ResponseEntity<>(productService.getAllProducts(pageable), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "특정 제품 정보 조회 api")
    @ApiResponse(responseCode = "200", description = "특정 상품 정보 조회 성공")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "새 상품 추가 api")
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "[관리자] 상품 정보 수정 api")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable("productId") Long productId, @Valid @RequestBody UpdateProductRequest product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "[관리자] 상품 삭제 api")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }


    /*Option api*/
    @GetMapping("/{productId}/options")
    @Operation(summary = "특정 상품의 모든 옵션 조회 api")
    public ResponseEntity<List<ProductOptionResponse>> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getOptions(productId));
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "[관리자] 특정 상품의 옵션 추가 api")
    public ResponseEntity<MessageResponse> addOption(@PathVariable("productId") Long productId, @Valid @RequestBody AddOptionRequest addOptionRequest) {
        return ResponseEntity.ok(productService.addOption(productId, addOptionRequest));
    }

}

