package gift.controller.wishlist;

import gift.DTO.product.ProductResponse;
import gift.DTO.wishlist.WishResponse;
import gift.domain.Product;
import gift.service.MemberService;
import gift.service.TokenService;
import gift.service.WishlistService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
@RequestMapping("/api/wishes")
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
    public ResponseEntity<Page<WishResponse>> getWishlist(
        @RequestHeader("Authorization") String authorizationHeader,
        @PageableDefault(page = 0, size = 10)
        @SortDefault(sort = "createdDate", direction = Direction.DESC) Pageable pageable
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = tokenService.extractEmailFromToken(token);
        Page<WishResponse> pageWishResponse = wishlistService.getWishlistByEmail(email, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageWishResponse);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishResponse> addToWishlist(
        @RequestHeader("Authorization") String authorizationHeader,
        @PathVariable Long productId
    ) {
        String token = tokenService.getBearerTokenFromHeader(authorizationHeader);
        tokenService.validateToken(token);
        String email = tokenService.extractEmailFromToken(token);
        WishResponse wish = wishlistService.addWishlist(email, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wish);
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
