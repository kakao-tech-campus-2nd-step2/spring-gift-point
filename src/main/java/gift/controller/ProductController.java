package gift.controller;

import gift.dto.CategoryUpdateRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 관리", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
    	this.productService = productService;
    }
    
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 페이지네이션으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 목록 반환 성공")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(@PageableDefault(sort="name") Pageable pageable) {
    	Page<ProductResponse> products = productService.getProducts(pageable);
    	return ResponseEntity.status(HttpStatus.OK).body(products);
    }
    
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 상세 정보 반환 성공")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId) {
    	ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
    
    @Operation(summary = "상품 추가", description = "새 상품을 추가합니다.")
    @ApiResponse(responseCode = "201", description = "상품 추가 성공")
    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductRequest request, BindingResult bindingResult) {
        productService.createProduct(request, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") Long productId, 
    		@Valid @RequestBody ProductUpdateRequest request, BindingResult bindingResult) {
        productService.updateProduct(productId, request, bindingResult);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @Operation(summary = "상품 카테고리 업데이트", description = "상품의 카테고리를 업데이트합니다.")
    @ApiResponse(responseCode = "200", description = "상품 카테고리 업데이트 성공")
    @PutMapping("/{product_id}/category")
    public ResponseEntity<Void> updateProductCategory(@PathVariable("product_id") Long product_id,
    		@Valid @RequestBody CategoryUpdateRequest request, BindingResult bindingResult){
    	productService.updateProductCategory(product_id, request, bindingResult);
    	return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    @DeleteMapping("/{product_id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("product_id") Long product_id) {
        productService.deleteProduct(product_id);
        return ResponseEntity.status(HttpStatus.OK).build();
    } 
}
