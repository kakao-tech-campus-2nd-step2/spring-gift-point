package gift.main.controller;

import gift.main.annotation.SessionUser;
import gift.main.dto.UserVo;
import gift.main.dto.WishProductResponse;
import gift.main.service.WishProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    private final WishProductService wishProductService;

    public WishlistController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @DeleteMapping("/wishlist/{productId}")
    @Transactional
    public ResponseEntity<?> deleteWishProduct(@PathVariable(name = "productId") Long productId, @SessionUser UserVo sessionUserVo) {
        wishProductService.deleteProducts(productId, sessionUserVo);
        return ResponseEntity.ok("successfully deleted the item to your wishlist");
    }

    @GetMapping("/wishlists")
    public ResponseEntity<?> getWishProductPage(@SessionUser UserVo sessionUserVo, Pageable pageable) {
        Page<WishProductResponse> wishProductPage = wishProductService.getWishProductPage(sessionUserVo, pageable);
        return ResponseEntity.ok(wishProductPage);
    }

    @Transactional
    @PostMapping("/wishlist/{productId}")
    public ResponseEntity<?> addWishlistProduct(@PathVariable(name = "productId") Long productId, @SessionUser UserVo sessionUser) {
        wishProductService.addWishlistProduct(productId, sessionUser);
        return ResponseEntity.ok("successfully added the item to your wishlist");
    }

}
