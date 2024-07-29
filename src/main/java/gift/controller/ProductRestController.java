package gift.controller;

import gift.dto.request.OptionRequest;
import gift.dto.request.ProductOptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;
    private final OptionService optionService;

    public ProductRestController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @Operation(summary = "새로운 제품과 옵션을 추가합니다")
    @PostMapping
    public ResponseEntity<Void> add(@Valid @RequestBody ProductOptionRequest productOptionRequest) {
        productService.save(productOptionRequest.getProductRequest(), productOptionRequest.getOptionRequest());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "ID로 제품을 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @Operation(summary = "페이징된 제품 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getPagedProducts(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getPagedProducts(pageable));
    }

    @Operation(summary = "ID로 제품을 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "ID로 제품 정보를 업데이트합니다")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        productService.updateById(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "ID로 제품의 옵션 목록을 조회합니다")
    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponse>> getProductOptions(@PathVariable Long id) {
        productService.findById(id);
        return ResponseEntity.ok().body(optionService.findByProductId(id));
    }

    @Operation(summary = "ID로 제품에 옵션을 추가합니다")
    @PostMapping("/{id}/options")
    public ResponseEntity<Void> addProductOption(@PathVariable Long id, @Valid @RequestBody OptionRequest optionRequest){
        optionService.save(id, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}