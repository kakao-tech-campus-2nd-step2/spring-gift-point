package gift.controller;

import gift.controller.dto.PaginationDTO;
import gift.controller.dto.WishRequest;
import gift.controller.dto.WishResponse;
import gift.domain.Wish;
import gift.service.WishService;
import gift.utils.JwtTokenProvider;
import gift.utils.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<WishResponse> addToWishlist(@RequestBody WishRequest wishRequest,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        WishResponse wishResponse = wishlistService.addToWishlist(email, wishRequest);
        return ResponseEntity.ok(wishResponse);

    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<WishResponse> removeFromWishlist(@PathVariable("wishId") Long wishId,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        WishResponse wishResponse = wishlistService.removeFromWishlist(email, wishId);
        return ResponseEntity.ok(wishResponse);

    }

    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishlist(
        @RequestHeader("Authorization") String token,
        @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {


        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        Page<WishResponse> wishlistProducts = wishlistService.getWishlistProducts(email, pageable);
        return ResponseEntity.ok(wishlistProducts);
    }

    @PutMapping()
    public ResponseEntity<WishResponse> changeToWishlist(@RequestBody WishRequest wishRequest,
        @RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
        WishResponse wishResponse = wishlistService.changeToWishlist(email, wishRequest);
        return ResponseEntity.ok(wishResponse);

    }


}
