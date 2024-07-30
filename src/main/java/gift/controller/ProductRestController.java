package gift.controller;

import gift.model.option.OptionRequest;
import gift.model.option.OptionResponse;
import gift.model.product.Product;
import gift.model.product.ProductRequest;
import gift.model.product.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 API", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductRestController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    // 모든 상품 조회
    @Operation(summary = "상품 조회", description = "등록된 상품을 모두 조회한다.")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
        @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 특정 상품 조회
    @Operation(summary = "Id별 상품 조회", description = "Id를 받고 그에 맞는 상품을 찾는다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Operation(summary = "특정 카테고리별 상품 조회", description = "카테고리별로 상품들을 조회한다.")
    @GetMapping("/categories/{category_id}")
    public ResponseEntity<List<ProductResponse>> getProductByCategory(
        @RequestParam("category_id") Long categoryId) {
        List<ProductResponse> products = productService.getProductByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    // 상품 추가
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가한다.")
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest product, @RequestBody OptionRequest option) {
        ProductResponse createdProduct = productService.createProduct(product, option);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // 상품 수정
    @Operation(summary = "상품 정보 수정", description = "상품 정보를 수정한다.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest product) {
        try {
            productService.updateProduct(id, product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 상품 삭제
    @Operation(summary = "상품 삭제", description = "Id를 받아서 상품을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 상품 옵션 조회, 추가, 수정, 삭제
    @Operation(summary = "상품 옵션 조회", description = "상품 옵션을 조회한다.")
    @GetMapping("/{product_id}/options")
    public ResponseEntity<List<OptionResponse>> getOptionsByProductId(@PathVariable("product_id") Long productId) {
        List<OptionResponse> options = optionService.getOptionByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @Operation(summary = "id별 상품 옵션 추가", description = "Id를 받고 상품 옵션을 조회한다.")
    @PostMapping("/{id}/options")
    public ResponseEntity<OptionResponse> addOption(@PathVariable("id") Long productId,
        @RequestBody OptionRequest optionRequest) {
            OptionResponse optionResponse = optionService.addOption(productId, optionRequest);
            return ResponseEntity.ok(optionResponse);
    }

    @Operation(summary = "상품 옵션 수정", description = "상품 옵션을 수정한다.")
    @PutMapping("/{id}/options/{optionId}")
    public ResponseEntity<OptionResponse> updateOption(@PathVariable("id") Long productId,
        @PathVariable("optionId") Long optionId, @RequestBody OptionRequest optionRequest) {
        OptionResponse optionResponse = optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.ok(optionResponse);
    }

    @Operation(summary = "상품 옵션 삭제", description = "상품 옵션을 삭제한다.")
    @DeleteMapping("/{id}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable("id") Long productId,
        @PathVariable("optionId") Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }


}


