package gift.controller;

import gift.model.CurrentMember;
import gift.model.Member;
import gift.model.Wishlist;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Tag(name = "Wishlist", description = "위시리스트 관련 api")
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    @Operation(summary = "회원의 모든 위시리스트 조회", description = "회원의 모든 위시리스트를 조회합니다.")
    public ResponseEntity<Page<Wishlist>> getWishlist(
            @CurrentMember Member member,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Wishlist> wishlistPage = wishlistService.getWishlist(member.getId(), pageable);
        return ResponseEntity.ok(wishlistPage);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "상품 id로 위시리스트 추가", description = "상품 id로 위시리스트 추가합니다.")
    public ResponseEntity<Void> addProductToWishlist(@CurrentMember Member member, @PathVariable Long productId) {
        wishlistService.addProductToWishlist(member.getId(), productId);
        return ResponseEntity.created(URI.create("/api/wishlist/" + productId)).build();
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 id로 위시리스트 삭제", description = "상품 id로 위시리스트 삭제합니다.")
    public ResponseEntity<Void> removeProductFromWishlist(@CurrentMember Member member, @PathVariable Long productId) {
        wishlistService.removeProductFromWishlist(member.getId(), productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
