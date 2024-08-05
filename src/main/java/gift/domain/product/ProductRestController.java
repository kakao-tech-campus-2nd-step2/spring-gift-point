package gift.domain.product;

import gift.domain.product.dto.request.ProductRequest;
import gift.domain.product.dto.request.UpdateProductRequest;
import gift.domain.product.dto.response.ProductPageResponse;
import gift.domain.product.dto.response.ProductResponse;
import gift.global.response.ResponseMaker;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product API")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 상품 생성
     */
    @PostMapping
    @Operation(summary = "상품 생성")
    public ResponseEntity<SimpleResultResponseDto> createProduct(
        @Valid @RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 조회
     */
    @GetMapping("{productId}")
    @Operation(summary = "특정 상품 조회")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long id) {
        Product product = productService.getProduct(id);
        ProductResponse productResponse = product.toProductResponse();
        return ResponseEntity.ok(productResponse);
    }

    /**
     * 모든 상품 조회 - 페이징
     */
    @GetMapping
    @Operation(summary = "모든 상품 조회 - 페이징")
    public ResponseEntity<ProductPageResponse> getProductsByPageAndSort(
        @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "name_asc") String sort,
        @Parameter(description = "카테고리 ID") @RequestParam(value = "categoryId", defaultValue = "0") Long categoryId
    ) {
        Sort sortObj = getSortObject(sort);
        ProductPageResponse productPageResponse = productService.getProductsByPage(page, size, sortObj,
            categoryId);
        return ResponseEntity.ok(productPageResponse);
    }

    /**
     * 상품 수정 - (현재) 상품의 옵션은 "따로" 수정
     */
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정")
    public ResponseEntity updateProduct(
        @Parameter(description = "상품 ID") @PathVariable("id") Long id,
        @Valid @RequestBody UpdateProductRequest productRequest
    ) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity deleteProduct(
        @Parameter(description = "상품 ID") @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 선택된 상품들 삭제
     */
    @DeleteMapping
    @Operation(summary = "선택된 상품들 삭제")
    public ResponseEntity deleteSelectedProducts(
        @RequestBody List<Long> productIds) {
        productService.deleteProductsByIds(productIds);
        return ResponseEntity.ok().build();
    }


    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "name_asc":
                return Sort.by(Sort.Direction.ASC, "name");
            case "name_desc":
                return Sort.by(Sort.Direction.DESC, "name");
            default:
                return Sort.by(Sort.Direction.ASC, "id");
        }
    }
}
