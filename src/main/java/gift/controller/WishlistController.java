package gift.controller;

import gift.service.MemberService;
import gift.service.ProductService;
import gift.service.WishlistService;
import gift.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist API", description = "위시리스트 관련 API")
public class WishlistController {

    private final WishlistService wishlistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WishlistController(WishlistService wishlistService,JwtTokenProvider jwtTokenProvider) {
        this.wishlistService = wishlistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "위시리스트 아이템 삭제", description = "위시리스트에서 특정 아이템을 삭제합니다.")
    public ResponseEntity<Void> deleteWishlistItem(@RequestHeader("Authorization") String token, @PathVariable Long productId) {
        String email = jwtTokenProvider.getEmail(token.substring(7));
        wishlistService.deleteWishlistItem(email, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}")
    @Operation(summary = "위시리스트 아이템 추가", description = "위시리스트에 특정 아이템을 추가합니다.")
    public ResponseEntity<Void> addWishlistItem(@RequestParam("email") String email, @PathVariable Long productId) {
        wishlistService.addWishlistItem(email, productId);
        return ResponseEntity.ok().build();
    }
}
