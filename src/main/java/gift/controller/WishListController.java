package gift.controller;

import gift.ArgumentResolver.LoginMember;
import gift.dto.MemberRequest;
import gift.dto.WishListRequest;
import gift.dto.WishListResponse;
import gift.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/wishlist")
@RestController
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<WishListResponse> getWishList(@LoginMember MemberRequest memberRequest) {
        return ResponseEntity.ok(wishListService.getWishList(memberRequest.getId()));
    }

    //상품 추가
    @PostMapping
    public ResponseEntity<Void> addWishList(@LoginMember MemberRequest memberRequest,
        @RequestBody WishListRequest wishListRequest) {
        wishListService.addProduct(memberRequest.getId(), wishListRequest.getProductId());
        return ResponseEntity.ok().build();
    }

    //상품 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteWishList(@LoginMember MemberRequest memberRequest,
        @RequestBody WishListRequest wishListRequest) {
        wishListService.deleteProduct(memberRequest.getId(), wishListRequest.getProductId());
        return ResponseEntity.ok().build();
    }

    //상품 수정
    @PutMapping
    public ResponseEntity<Void> updateWishList(@LoginMember MemberRequest memberRequest,
        @RequestBody WishListRequest wishListRequest) {
        wishListService.updateProduct(memberRequest.getId(), wishListRequest.getProductId(),
            wishListRequest.getProductCount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{page}")
    public ResponseEntity<WishListResponse> getWishListPage(@LoginMember MemberRequest memberRequest,
        @PathVariable int page) {
        return ResponseEntity.ok(wishListService.getWishListPage(memberRequest.getId(), page, 10));
    }


}
