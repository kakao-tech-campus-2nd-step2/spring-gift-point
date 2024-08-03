package gift.controller.restcontoller;

import gift.dto.request.OptionRequest;
import gift.dto.request.ProductOptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.CommonResponse;
import gift.dto.response.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;
    private final OptionService optionService;

    public ProductRestController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @Operation(summary = "새로운 상품과 옵션을 생성합니다")
    @PostMapping
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductOptionRequest productOptionRequest) {
        productService.save(productOptionRequest.getProductRequest(), productOptionRequest.getOptionRequest());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 ID로 상품을 조회합니다")
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long productId) {
        ProductResponse product = productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(product, "상품 ID로 상품 조회 성공", true));
    }

    @Operation(summary = "상품 ID로 상품 정보를 수정합니다")
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequest request) {
        productService.updateById(productId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 ID로 상품을 삭제합니다")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "카테고리별 모든 상품 정보를 조회합니다")
    @GetMapping("/categories")
    public ResponseEntity<CommonResponse> getProductByCategoryId(@RequestParam(defaultValue = "1") Long categoryId){
        return ResponseEntity.ok().body(new CommonResponse(productService.findByCategoryId(categoryId),"카테고리별 상품 조회 성공", true));
    }

    @Operation(summary = "페이징된 카테고리별 상품 목록을 조회합니다")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CommonResponse> getPagedProductsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "price,asc") String sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(productService.getPagedProductsByCategoryId(page,size,sort,categoryId),"페이징 된 카테고리별 상품 목록 조회 성공",true));
    }

    @Operation(summary = "페이징된 상품 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getPagedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(productService.getPagedProducts(page,size,sort),"페이징 된 상품 목록 조회 성공",true));
    }

    @Operation(summary = "상품 ID로 상품에 옵션을 추가합니다")
    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> addProductOption(@PathVariable Long productId, @Valid @RequestBody OptionRequest optionRequest){
        optionService.save(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "옵션 ID로 옵션을 수정합니다")
    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> updateProductOption(@PathVariable Long optionId, @Valid @RequestBody OptionRequest optionRequest){
        optionService.updateById(optionId, optionRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "옵션 ID로 옵션을 삭제합니다")
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteProductOption(@PathVariable Long optionId){
        optionService.deleteById(optionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "상품 ID로 상품의 옵션 목록을 조회합니다")
    @GetMapping("/{productId}/options")
    public ResponseEntity<CommonResponse> getProductOptions(@PathVariable Long productId) {
        productService.findById(productId);
        return ResponseEntity.ok().body(new CommonResponse(optionService.findByProductId(productId),"상품의 옵션 목록 조회 성공", true));
    }
}