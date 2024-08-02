package gift.product.controller;

import gift.product.docs.WishListControllerDocs;
import gift.product.dto.WishRequestDTO;
import gift.product.dto.WishResponseDTO;
import gift.product.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class ApiWishListController implements WishListControllerDocs {

    private final WishListService wishListService;

    @Autowired
    public ApiWishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public Page<WishResponseDTO> showProductList(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        System.out.println("[ApiWishListController] showProductList()");
        return wishListService.getAllWishes(authorization, pageable);
    }

    @PostMapping
    public ResponseEntity<WishResponseDTO> registerWishProduct(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody WishRequestDTO wishRequestDTO) {
        System.out.println("[ApiWishListController] registerWishProduct()");
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishListService.registerWishProduct(authorization, wishRequestDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteWishProduct(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long productId) {
        System.out.println("[ApiWishListController] deleteWishProduct()");
        wishListService.deleteWishProduct(authorization, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
