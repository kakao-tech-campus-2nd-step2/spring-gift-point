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
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductManageController", description = "상품 관리 관련 API")
@RestController
@RequestMapping("/api")
public class ProductManageController {

    private final ProductService productService;

    public ProductManageController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 조회", description = "상품을 조회합니다.")
    @GetMapping("/products")
    public ResponseEntity<?> getProducts(
            @RequestParam @Nullable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort sorting = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);

        return ResponseEntity.ok(productService.getProductByCategory(categoryId, pageable));
    }

    @Operation(summary = "상품 단건 조회", description = "특정 상품을 조회합니다.")
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(new CommonResponse<>(productService.getProductById(id), "상품 조회 성공", true));
    }

    @Operation(summary = "상품 옵션 조회", description = "특정 상품의 옵션을 조회합니다.")
    @GetMapping("/products/{id}/options")
    public ResponseEntity<?> getProductOptions(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductOptions(id));
    }


    //    @AdminAuthenticated
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    @PostMapping("/products/create")
    public ResponseEntity<?> addProduct(
            @Valid @RequestBody CreateProductRequestDTO createProductRequestDTO) {
        Long productId = productService.saveProduct(createProductRequestDTO);
        return ResponseEntity.ok(new CommonResponse<>(productId, "상품이 정상적으로 추가 되었습니다", true));
    }

    //    @AdminAuthenticated
    @Operation(summary = "상품 옵션 추가", description = "기존 상품에 옵션을 추가합니다.")
    @PostMapping("/products/{id}")
    public ResponseEntity<?> addProductOption(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @RequestBody CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        Product product = productService.addProductOption(id, createProductOptionRequestDTO);

        return ResponseEntity.ok(new CommonResponse<>(product.getProductOptions(), "상품 옵션이 정상적으로 추가 되었습니다", true));
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new CommonResponse<>(null, "상품이 정상적으로 삭제 되었습니다", true));
    }


}
