package gift.controller;

import gift.domain.AppUser;
import gift.dto.product.CreateProductAdminRequest;
import gift.dto.product.UpdateProductRequest;
import gift.service.ProductAdminService;
import gift.service.ProductService;
import gift.util.aspect.AdminController;
import gift.util.resolver.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "관리자 권한으로 상품 추가", description = "키워드 제약 없이 상품 추가 가능")
    @PostMapping
    public ResponseEntity<String> addProductForAdmin(@LoginUser AppUser loginAppUser,
                                                     @Valid @RequestBody CreateProductAdminRequest createProductAdminRequest) {
        productAdminService.addProduct(createProductAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @Operation(summary = "관리자 권한으로 상품 수정", description = "판매자가 아니더라도 상품 수정 가능")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                        @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProduct(loginAppUser, id, updateProductRequest);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "관리자 권한으로 상품 삭제", description = "판매자가 아니더라도 상품 삭제 가능")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        productService.deleteProduct(loginAppUser, id);
        return ResponseEntity.ok().body("ok");
    }

    @Operation(summary = "관리자 권한으로 상품 카테고리 수정", description = "관리자만 상품 카테고리 수정 가능")
    @PutMapping("/{productId}/category")
    public ResponseEntity<String> updateCategoryForProduct(@LoginUser AppUser loginAppUser,
                                                           @PathVariable Long productId,
                                                           @RequestParam Long categoryId) {
        productAdminService.updateCategory(productId, categoryId);
        return ResponseEntity.ok().body("ok");
    }
}

