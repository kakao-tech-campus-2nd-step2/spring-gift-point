package gift.controller.product;

import gift.common.dto.PageResponse;
import gift.controller.product.dto.ProductRequest;
import gift.controller.product.dto.ProductResponse;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@Tag(name = "Product", description = "상품 API")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping("")
    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    public ResponseEntity<Void> registerProduct(@Valid @RequestBody ProductRequest.Create request) {
        Long id = productService.addProduct(request);
        return ResponseEntity.created(URI.create("/api/v1/products/" + id)).build();
    }

    @GetMapping("")
    @Operation(summary = "전체 상품 조회", description = "전체 상품을 조회합니다.")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
        @RequestParam(value = "categoryId", required = false) Long categoryId,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<ProductResponse> response = productService.findAllProduct(categoryId, pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 조회", description = "상품을 조회합니다.")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse response = productService.findProduct(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("id") Long id,
                                                         @Valid @RequestBody ProductRequest.Update request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

