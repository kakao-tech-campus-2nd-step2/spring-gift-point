package gift.product.controller;

import gift.product.domain.ProductRequest;
import gift.product.option.domain.OptionDTO;
import gift.product.option.service.OptionService;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping("")
    @Operation(summary = "상품 생성", tags = {"상품 API"})
    public ResponseEntity<ProductRequest> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductRequest response = productService.createProduct(productRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("")
    @Operation(summary = "상품 목록 조회", tags = {"상품 API"})
    public ResponseEntity<Page<ProductRequest>> getAllProductsWishCategoryId(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name,asc") String sort,
        @RequestParam(required = false) Long categoryId) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<ProductRequest> products;
        if (categoryId != null) {
            products = productService.getAllProductsByCategoryId(categoryId, pageable);
        } else {
            products = productService.getAllProducts(pageable);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", tags = {"상품 API"})
    public ResponseEntity<ProductRequest> findById(@PathVariable("productId") Long id) {
        Optional<ProductRequest> productDTO = productService.getProductDTOById(id);
        return productDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", tags = {"상품 API"})
    public ResponseEntity<ProductRequest> updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody ProductRequest productRequest) {
        Optional<ProductRequest> existingProductDTO = productService.getProductDTOById(id);
        if (existingProductDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", tags = {"상품 API"})
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        if (productService.getProductDTOById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Option 관련 메서드
    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회", tags = {"상품 옵션 API"})
    public ResponseEntity<List<OptionDTO>> getOptionList(@PathVariable Long productId) {
        return new ResponseEntity<>(optionService.findAllByProductId(productId), HttpStatus.OK);
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", tags = {"상품 옵션 API"})
    public ResponseEntity<OptionDTO> addOption(@PathVariable Long productId, @RequestBody OptionDTO optionDTO) {
        try {
            optionDTO = optionService.addOption(productId, optionDTO);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(optionDTO);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 수정", tags = {"상품 옵션 API"})
    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionDTO optionDTO) {
        try {
            optionDTO = optionService.updateOption(productId, optionId, optionDTO);
            return new ResponseEntity<>(optionDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제", tags = {"상품 옵션 API"})
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
