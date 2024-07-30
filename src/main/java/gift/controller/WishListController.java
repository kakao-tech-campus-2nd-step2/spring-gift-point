package gift.controller;

import gift.config.LoginMember;
import gift.dto.MemberRequestDTO;
import gift.model.WishList;
import gift.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 사용자의 위시 리스트를 조회
    @GetMapping
    public List<WishList> getWishlist(@LoginMember MemberRequestDTO memberRequestDTO, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return wishListService.getWishlist(memberRequestDTO, page, size);
    }

    // 위시 리스트에 상품을 추가
    @PostMapping
    public void addProductToWishlist(@LoginMember MemberRequestDTO memberRequestDTO, @RequestParam("productId") Long productId) {
        wishListService.addProductToWishlist(memberRequestDTO, productId);
    }

    // 위시 리스트에서 상품을 삭제
    @DeleteMapping("/{id}")
    public void removeProductFromWishlist(@LoginMember MemberRequestDTO memberRequestDTO, @PathVariable("id") Long id) {
        wishListService.removeProductFromWishlist(memberRequestDTO, id);
    }
}
