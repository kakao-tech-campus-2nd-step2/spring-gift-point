package gift.product.controller;

import gift.aspect.AdminController;
import gift.product.model.dto.product.CreateProductAdminRequest;
import gift.product.model.dto.product.UpdateProductRequest;
import gift.product.service.ProductAdminService;
import gift.product.service.ProductService;
import gift.resolver.LoginUser;
import gift.user.model.dto.AppUser;
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
public class ProductAdminController {

    private final ProductService productService;
    private final ProductAdminService productAdminService;

    public ProductAdminController(ProductService productService, ProductAdminService productAdminService) {
        this.productService = productService;
        this.productAdminService = productAdminService;
    }

    @PostMapping
    public ResponseEntity<String> addProductForAdmin(@LoginUser AppUser loginAppUser,
                                                     @Valid @RequestBody CreateProductAdminRequest createProductAdminRequest) {
        productAdminService.addProduct(createProductAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                        @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProduct(loginAppUser, id, updateProductRequest);
        return ResponseEntity.ok().body("ok");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        productService.deleteProduct(loginAppUser, id);
        return ResponseEntity.ok().body("ok");
    }

    @PutMapping("/{productId}/category")
    public ResponseEntity<String> updateCategoryForProduct(@LoginUser AppUser loginAppUser,
                                                           @PathVariable Long productId,
                                                           @RequestParam Long categoryId) {
        productAdminService.updateCategory(productId, categoryId);
        return ResponseEntity.ok().body("ok");
    }
}

