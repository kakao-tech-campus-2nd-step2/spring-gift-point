package gift.controller;

import gift.Login;
import gift.dto.LoginMember;
import gift.dto.WishProduct;
import gift.dto.response.MessageResponse;
import gift.dto.response.WishProductResponse;
import gift.service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<Page<WishProductResponse>> getWishList(@Login LoginMember member, Pageable pageable) {
        Page<WishProductResponse> wishes = wishListService.getWishList(member.getId(), pageable);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<MessageResponse> addWishProduct(@Login LoginMember member, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishListService.addWishProduct(new WishProduct(member.getId(), productId)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<MessageResponse> deleteWishProduct(@Login LoginMember member, @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(wishListService.deleteWishProduct(new WishProduct(member.getId(), productId)));
    }
}
