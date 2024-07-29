package gift.controller;

import gift.controller.dto.PaginationDTO;
import gift.controller.dto.WishRequest;
import gift.domain.Wish;
import gift.service.WishService;
import gift.utils.JwtTokenProvider;
import gift.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/wishs")
public class WishController {

    private final WishService wishlistService;
    private final JwtTokenProvider jwtTokenProvider;

    public WishController(WishService wishlistService, JwtTokenProvider jwtTokenProvider) {
        this.wishlistService = wishlistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping()
    public ResponseEntity<String> addToWishlist(@RequestBody WishRequest wishRequest,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        wishlistService.addToWishlist(email, wishRequest);
        return ResponseEntity.ok("Product added to wishlist");

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Long productId,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        wishlistService.removeFromWishlist(email, productId);
        return ResponseEntity.ok("Product removed from wishlist");

    }

    @GetMapping
    public ResponseEntity<Page<Wish>> getWishlist(
        @RequestHeader("Authorization") String token,
        @ModelAttribute PaginationDTO paginationDTO) {

        Pageable pageable = PaginationUtils.createPageable(paginationDTO, "wishlist");

        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        Page<Wish> wishlistProducts = wishlistService.getWishlistProducts(email, pageable);
        return ResponseEntity.ok(wishlistProducts);
    }

    @PutMapping()
    public ResponseEntity<String> changeToWishlist(@RequestBody WishRequest wishRequest,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        wishlistService.changeToWishlist(email, wishRequest);
        return ResponseEntity.ok("Product added to wishlist");

    }


}
