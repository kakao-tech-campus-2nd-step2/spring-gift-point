package gift.wishlist.presentation;

import gift.member.presentation.request.ResolvedMember;
import gift.wishlist.application.WishlistService;
import gift.wishlist.application.response.WishlistSaveServiceResponse;
import gift.wishlist.presentation.request.WishlistCreateRequest;
import gift.wishlist.presentation.response.WishlistAddResponse;
import gift.wishlist.presentation.response.WishlistReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController implements WishlistApi {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistAddResponse> add(
            ResolvedMember resolvedMember,
            WishlistCreateRequest request
    ) {
        WishlistSaveServiceResponse wish = wishlistService.save(request.toCommand(resolvedMember.id()));

        return ResponseEntity.created(URI.create("/api/wishes/" + wish.id())).body(
                WishlistAddResponse.of(wish.id(), wish.memberId(), wish.productId())
        );
    }

    @GetMapping
    public ResponseEntity<Page<WishlistReadResponse>> findAll(
            ResolvedMember resolvedMember,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                wishlistService.findAllByMemberId(resolvedMember.id(), pageable).map(WishlistReadResponse::from)
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long wishId) {
        wishlistService.delete(wishId);
    }
}
