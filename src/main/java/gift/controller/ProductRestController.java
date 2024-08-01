package gift.controller;

import gift.dto.CommonResponse;
import gift.dto.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductRestController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @Operation(summary = "상품 ID로 상품을 조회합니다")
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long productId) {
        ProductResponse product = productService.findById(productId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse<>(product, "상품 ID로 상품 조회 성공", true));
    }

    @Operation(summary = "페이징된 상품 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getPagedProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name,asc") String sort,
        @RequestParam(defaultValue = "1") Long categoryId) {

        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        System.out.println(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse(productService.getPagedProducts(pageable, categoryId),
                "상품 목록 페이지 조회 성공", true));
    }

    @Operation(summary = "상품 ID로 상품의 옵션 목록을 조회합니다")
    @GetMapping("/{productId}/options")
    public ResponseEntity<CommonResponse> getProductOptions(@PathVariable Long productId) {
        productService.findById(productId);
        return ResponseEntity.ok().body(new CommonResponse(optionService.findByProductId(productId),"상품의 옵션 목록 조회 성공", true));
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

}