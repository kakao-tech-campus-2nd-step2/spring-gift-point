package gift.controller;

import static gift.util.Utils.DEFAULT_PAGE_SIZE;

import gift.domain.AppUser;
import gift.dto.product.CreateProductRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.UpdateProductRequest;
import gift.service.ProductService;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product User API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 Id로 상품 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
        final ProductResponse response = productService.findProductWithWishCount(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 전체 조회", description = "상품 전체 조회 정보를 Page로 반환")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAllProductPage(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "wishCount", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductResponse> response = productService.findAllProductWithWishCountPageable(pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "카테고리 별 상품 전체 조회", description = "상품 전체 조회 정보를 Page로 반환")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> findActiveProductsByCategoryWithWishCount(
            @PathVariable Long categoryId,
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "wishCount", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductResponse> response = productService.findActiveProductsByCategoryWithWishCount(categoryId, pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 추가", description = "`카카오` 키워드 사용 제약")
    @PostMapping
    public ResponseEntity<String> addProduct(@LoginUser AppUser loginAppUser,
                                             @Valid @RequestBody CreateProductRequest createProductRequest) {
        productService.addProduct(loginAppUser, createProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @Operation(summary = "상품 수정", description = "판매자만 접근 가능")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProduct(loginAppUser, id, updateProductRequest);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "상품 삭제", description = "판매자만 접근 가능")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        productService.deleteProduct(loginAppUser, id);
        return ResponseEntity.ok().body("ok");
    }
}
