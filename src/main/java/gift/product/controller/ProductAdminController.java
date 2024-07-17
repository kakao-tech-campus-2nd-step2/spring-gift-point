package gift.product.controller;

import gift.product.model.dto.CreateProductAdminRequest;
import gift.product.model.dto.CreateProductRequest;
import gift.product.model.dto.UpdateProductRequest;
import gift.product.service.ProductService;
import gift.user.model.dto.AppUser;
import gift.user.resolver.LoginUser;
import gift.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/admin")
public class ProductAdminController {

    private final ProductService productService;
    private final UserService userService;

    public ProductAdminController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> addProductForAdmin(@LoginUser AppUser loginAppUser,
                                                     @Valid @RequestBody CreateProductAdminRequest createProductRequest) {
        userService.verifyAdminAccess(loginAppUser);
        CreateProductRequest req = new CreateProductRequest(createProductRequest.name(), createProductRequest.price(),
                createProductRequest.imageUrl());

        AppUser seller = userService.findUser(createProductRequest.sellerId());

        productService.addProduct(seller, req);
        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id,
                                                        @Valid @RequestBody UpdateProductRequest updateProductRequest) {
        userService.verifyAdminAccess(loginAppUser);
        productService.updateProduct(loginAppUser, id, updateProductRequest);
        return ResponseEntity.ok().body("ok");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductByIdForAdmin(@LoginUser AppUser loginAppUser, @PathVariable Long id) {
        userService.verifyAdminAccess(loginAppUser);
        productService.deleteProduct(loginAppUser, id);
        return ResponseEntity.ok().body("ok");
    }
}
