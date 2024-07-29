package gift.wishlist.presentation;

import gift.member.presentation.request.ResolvedMember;
import gift.wishlist.application.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController implements WishlistApi {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("")
    public void add(ResolvedMember resolvedMember, @RequestParam("productId") Long productId) {
        wishlistService.save(resolvedMember.id(), productId);
    }

    @GetMapping("")
    public ResponseEntity<Page<WishlistControllerResponse>> findAll(
            ResolvedMember resolvedMember,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                wishlistService.findAllByMemberId(resolvedMember.id(), pageable).map(WishlistControllerResponse::from)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Page<WishlistControllerResponse>> findAllByProductId(
            @PathVariable("productId") Long productId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                wishlistService.findAllByProductId(productId, pageable).map(WishlistControllerResponse::from)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long wishlistId) {
        wishlistService.delete(wishlistId);
    }
}
