package gift.main.controller;

import gift.main.annotation.SessionUser;
import gift.main.dto.ProductId;
import gift.main.dto.UserVo;
import gift.main.dto.WishProductResponse;
import gift.main.service.WishProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishProductService wishProductService;

    public WishlistController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    //특정 페이지의 위시 목록 조회
    @GetMapping()
    public ResponseEntity<?> getWishProductPage(@RequestParam(value = "page") int pageNum, @SessionUser UserVo sessionUserVo) {
        Page<WishProductResponse> wishProductPage = wishProductService.getWishProductPage(sessionUserVo, pageNum);
        return ResponseEntity.ok(wishProductPage);
    }

    //새로운 위시 추가
    @PostMapping()
    public ResponseEntity<?> addWishlistProduct(@RequestBody ProductId productId, @SessionUser UserVo sessionUser) {
        wishProductService.addWishlistProduct(productId.productId(), sessionUser);
        return ResponseEntity.ok("successfully added the item to your wishlist");
    }

    //위시 삭제
    @DeleteMapping("/{wish_id}")
    public ResponseEntity<?> deleteWishProduct(@PathVariable(name = "wish_id") Long wishId, @SessionUser UserVo sessionUserVo) {
        wishProductService.deleteWishProduct(wishId);
        return ResponseEntity.ok("successfully deleted the item to your wishlist");
    }

}
