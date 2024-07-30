package gift.product.presentation;

import gift.product.application.ProductService;
import gift.product.domain.CreateProductOptionRequestDTO;
import gift.product.domain.CreateProductRequestDTO;
import gift.product.domain.Product;
import gift.util.CommonResponse;
import gift.util.annotation.AdminAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductManageController", description = "상품 관리 관련 API")
@RestController
@RequestMapping("/api/v1/product")
public class ProductManageController {

    private final ProductService productService;

    public ProductManageController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 조회", description = "모든 상품을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<Product>>> getProducts() {
        List<Product> productList = productService.getProduct();
        return ResponseEntity.ok(new CommonResponse<>(productList, "상품 조회가 정상적으로 완료되었습니다", true));
    }

    @AdminAuthenticated
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping("")
    public ResponseEntity<CommonResponse<Long>> addProduct(
            @Valid @RequestBody CreateProductRequestDTO createProductRequestDTO) {
        Long productId = productService.saveProduct(createProductRequestDTO);
        return ResponseEntity.ok(new CommonResponse<>(productId, "상품이 정상적으로 추가 되었습니다", true));
    }

    @AdminAuthenticated
    @Operation(summary = "상품 옵션 추가", description = "기존 상품에 옵션을 추가합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> addProductOption(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @RequestBody CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        productService.addProductOption(id, createProductOptionRequestDTO);
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 옵션이 정상적으로 추가 되었습니다", true));
    }

    @AdminAuthenticated
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new CommonResponse<>(null, "상품이 정상적으로 삭제 되었습니다", true));
    }
}
