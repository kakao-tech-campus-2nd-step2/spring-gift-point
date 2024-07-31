package gift.controller;

import gift.domain.AppUser;
import gift.dto.common.CommonResponse;
import gift.dto.product.CreateProductAdminRequest;
import gift.dto.product.UpdateProductRequest;
import gift.service.ProductAdminService;
import gift.service.ProductService;
import gift.util.aspect.AdminController;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AdminController
@RequestMapping("/api/admin/products")
@Tag(name = "Product", description = "Product Admin API")
public class ProductAdminController {

    private final ProductService productService;
    private final ProductAdminService productAdminService;

    public ProductAdminController(ProductService productService, ProductAdminService productAdminService) {
        this.productService = productService;
        this.productAdminService = productAdminService;
    }

    @Operation(summary = "관리자 권한으로 상품 추가", description = "키워드 제약 없이 상품 추가 가능",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @PostMapping
    public ResponseEntity<?> addProductForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                @Valid @RequestBody CreateProductAdminRequest createProductAdminRequest) {
        productAdminService.addProduct(createProductAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(null, "관리자 권한으로 상품 추가가 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 상품 수정", description = "판매자가 아니더라도 상품 수정 가능",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProduct(loginAppUser, id, updateProductRequest);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 상품 수정이 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 상품 삭제", description = "판매자가 아니더라도 상품 삭제 가능",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductByIdForAdmin(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                       @PathVariable Long id) {
        productService.deleteProduct(loginAppUser, id);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 상품 삭제가 완료되었습니다.", true));
    }

    @Operation(summary = "관리자 권한으로 상품 카테고리 수정", description = "관리자만 상품 카테고리 수정 가능",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "JWT token",
                            required = true,
                            in = ParameterIn.HEADER
                    )
            })
    @PutMapping("/{productId}/category")
    public ResponseEntity<?> updateCategoryForProduct(@Parameter(hidden = true) @LoginUser AppUser loginAppUser,
                                                      @PathVariable Long productId,
                                                      @RequestParam Long categoryId) {
        productAdminService.updateCategory(productId, categoryId);
        return ResponseEntity.ok(new CommonResponse<>(null, "관리자 권한으로 상품 카테고리 수정이 완료되었습니다.", true));
    }
}

