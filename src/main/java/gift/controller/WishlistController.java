package gift.controller;

import gift.entity.Wishlist;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "위시리스트", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<Page<Wishlist>> getAllWishlist(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Wishlist> wishlistPage = wishlistService.getAllWishlist(token, page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(wishlistPage.getNumber()));
        headers.add("X-Page-Size", String.valueOf(wishlistPage.getSize()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(wishlistPage);
    }

    @PostMapping
    public ResponseEntity<String> addWishlist(@RequestHeader("Authorization") String token, @RequestBody int product_id) {
        wishlistService.addItem(token, product_id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWishlist(@RequestHeader("Authorization") String token, @RequestBody int product_id) {
        wishlistService.deleteItem(token, product_id);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }

    @PutMapping
    public ResponseEntity<String> updateWishlist(@RequestHeader("Authorization") String token, @RequestBody int product_id, @RequestBody int num) {
        wishlistService.changeNum(token, product_id, num);
        return ResponseEntity.ok("상품 수량이 변경되었습니다.");
    }
}
