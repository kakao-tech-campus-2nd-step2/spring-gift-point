package gift.controller.wishlist;

import gift.DTO.product.ProductResponse;
import gift.domain.Product;
import gift.service.MemberService;
import gift.service.TokenService;
import gift.service.WishlistService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final TokenService tokenService;

    @Autowired
    public WishlistController(
        WishlistService wishlistService,
        TokenService tokenService
    ) {
        this.wishlistService = wishlistService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getWishlist(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "2") Integer size
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = tokenService.extractEmailFromToken(token);
        List<ProductResponse> wishlist = wishlistService.getWishlistByEmail(email, page, size);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addToWishlist(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable Long productId
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String email = tokenService.extractEmailFromToken(token);

        wishlistService.addWishlist(email, productId);
        return ResponseEntity.ok("Product added to wishlist");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable Long productId
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        String email = tokenService.extractEmailFromToken(token);

        wishlistService.removeWishlist(email, productId);
        return ResponseEntity.ok("Product removed from wishlist");
    }
}
